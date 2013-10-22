package de.eex.intranet.portal.portlet.hotnews.util;

import java.util.ArrayList;
import java.util.List;

import com.google.common.primitives.Longs;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQueryFactoryUtil;
import com.liferay.portal.kernel.dao.orm.PropertyFactoryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.model.Layout;
import com.liferay.portal.service.LayoutLocalServiceUtil;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PortalUtil;
import com.liferay.portlet.asset.model.AssetCategory;
import com.liferay.portlet.asset.model.AssetEntry;
import com.liferay.portlet.asset.service.AssetCategoryLocalServiceUtil;
import com.liferay.portlet.asset.service.AssetEntryLocalServiceUtil;
import com.liferay.portlet.asset.service.persistence.AssetEntryQuery;
import com.liferay.portlet.journal.model.JournalArticle;
import com.liferay.portlet.journal.model.JournalArticleResource;
import com.liferay.portlet.journal.service.JournalArticleLocalServiceUtil;
import com.liferay.portlet.journal.service.JournalArticleResourceLocalServiceUtil;

public class HotNewsUtil
{

	/**
	 * Get web content by category
	 * 
	 * @param groupId
	 * @param categoryName
	 * @param themeDisplay
	 * @return list of {@link JournalArticle}
	 * @throws SystemException
	 */
	public static List<JournalArticle> getArticleByCategory(	final long groupId,
																final List<Long> categoryIds,
																final ThemeDisplay themeDisplay ) throws SystemException
	{

		final AssetEntryQuery assetEntryQuery = new AssetEntryQuery();
		final List<JournalArticle> journalArticleList = new ArrayList<JournalArticle>();
		assetEntryQuery.setAllTagIds( new long[] {} );
		if ( !categoryIds.isEmpty() )
		{
			assetEntryQuery.setAnyCategoryIds( Longs.toArray( categoryIds ) );
		}
		else
		{
			// return all news that dont belongs to any categories
			assetEntryQuery.setNotAnyCategoryIds( Longs.toArray( getAllCategoryIds( themeDisplay ) ) );
		}
		final List<AssetEntry> assetEntryList = AssetEntryLocalServiceUtil.getEntries( assetEntryQuery );

		for ( final AssetEntry ae : assetEntryList )
		{
			if ( ae.getGroupId() == groupId )
			{
				if ( ae.getTags().size() > 0 )
				{
					continue;
				}
				try
				{
					final JournalArticleResource journalArticleResourceObj = JournalArticleResourceLocalServiceUtil.getJournalArticleResource( ae.getClassPK() );
					final JournalArticle journalArticleObj = JournalArticleLocalServiceUtil.getArticle( groupId,
																										journalArticleResourceObj.getArticleId() );
					journalArticleList.add( journalArticleObj );
				}
				catch ( final PortalException e )
				{
					// debug message not needed, this catch clause check if the
					// entry is a JournalArticle
				}
			}
		}

		return journalArticleList;
	}

	/**
	 * get categories that is assigned to liferay user
	 * 
	 * @param userFullname
	 * @param vocabularyId
	 * @return list of categories Id that is assigned to user
	 * @throws SystemException
	 */
	public static List<Long> getUserCategories( final String userFullname, final String vocabularyId ) throws SystemException
	{

		final List<Long> categoryIds = new ArrayList<Long>();

		final DynamicQuery query = DynamicQueryFactoryUtil.forClass( AssetEntry.class );
		query.add( PropertyFactoryUtil.forName( "title" ).eq( userFullname ) );
		final List<AssetEntry> entries = AssetEntryLocalServiceUtil.dynamicQuery( query );
		for ( final AssetEntry entry : entries )
		{
			for ( final AssetCategory category : entry.getCategories() )
			{
				if ( category.getVocabularyId() == Long.valueOf( vocabularyId ) )
				{
					categoryIds.add( category.getCategoryId() );
				}
			}
		}
		return categoryIds;
	}

	/**
	 * Get all categories from the site
	 * 
	 * @param {@link ThemeDisplay}
	 * @return list of all category ids belongs to the site
	 * @throws SystemException
	 */
	public static List<Long> getAllCategoryIds( final ThemeDisplay themeDisplay ) throws SystemException
	{

		final List<Long> categoryIds = new ArrayList<Long>();
		final DynamicQuery query = DynamicQueryFactoryUtil.forClass( AssetCategory.class );
		query.add( PropertyFactoryUtil.forName( "groupId" ).eq( themeDisplay.getScopeGroupId() ) );
		List<AssetCategory> categories;

		categories = AssetCategoryLocalServiceUtil.dynamicQuery( query );

		for ( final AssetCategory category : categories )
		{
			categoryIds.add( category.getCategoryId() );
		}
		return categoryIds;
	}

	/**
	 * Generate web content link
	 * 
	 * @param article
	 *            {@link JournalArticle}
	 * @param themeDisplay
	 *            {@link ThemeDisplay}
	 * @return redirect link to display web content
	 * @throws SystemException
	 * @throws PortalException
	 */
	public static String getJournalLink( final JournalArticle article, final ThemeDisplay themeDisplay ) throws SystemException, PortalException
	{

		// get the display page of the article
		final String layoutUuid = article.getLayoutUuid();
		if ( Validator.isNotNull( layoutUuid ) )
		{
			Layout layout = LayoutLocalServiceUtil.getLayoutByUuidAndGroupId( layoutUuid, article.getGroupId(), false );
			if ( Validator.isNull( layout ) )
			{
				layout = LayoutLocalServiceUtil.getLayoutByUuidAndGroupId( layoutUuid, article.getGroupId(), true );
			}
			if ( Validator.isNotNull( layout ) )
			{
				return PortalUtil.getLayoutFullURL( layout, themeDisplay ) + "/../-/" + article.getUrlTitle();
			}
		}
		// create a temporary asset publisher in maximized window mode to
		// display articles
		// that don't have an assigned display page.
		final String currentUrl = PortalUtil.getLayoutURL( themeDisplay.getLayout(), themeDisplay );

		final StringBuilder builder = new StringBuilder();
		builder.append( currentUrl );
		builder.append( "?p_p_id=101&p_p_lifecycle=0&p_p_state=maximized&p_p_mode=view&_101_struts_action=%2Fasset_publisher%2Fview_content" );
		builder.append( "&_101_returnToFullPageURL=" );
		builder.append( currentUrl );
		builder.append( "&_101_type=content&_101_urlTitle=" );
		builder.append( article.getUrlTitle() );
		builder.append( "&redirect=" );
		builder.append( currentUrl );
		return builder.toString();
	}
}
