#ifndef __ebook_h__
#define __ebook_h__

#include <string>
#include <android/log.h>

#include "sqlite3.h"
#include "sqlite3ext.h"

/**
 * @author andy
 *
 */
class EBook {

private:
	static const std::string DATABASE_TABLE_NAME;

	static const std::string FIELD_NAME_APPLICATION_ID;
	static const std::string FIELD_NAME_author_sort;
	static const std::string FIELD_NAME_authors;
	static const std::string FIELD_NAME_book_producer;
	static const std::string FIELD_NAME_BOOK_TITLE;
	static const std::string FIELD_NAME_category;
	static const std::string FIELD_NAME_comments;
	static const std::string FIELD_NAME_cover;
	static const std::string FIELD_NAME_db_id;
	static const std::string FIELD_NAME_ddc;
	static const std::string FIELD_NAME_isbn;
	static const std::string FIELD_NAME_language;
	static const std::string FIELD_NAME_lcc;
	static const std::string FIELD_NAME_lccn;
	static const std::string FIELD_NAME_lpath;
	static const std::string FIELD_NAME_mime;
	static const std::string FIELD_NAME_pubdate;
	static const std::string FIELD_NAME_publication_type;
	static const std::string FIELD_NAME_publisher;
	static const std::string FIELD_NAME_rating;
	static const std::string FIELD_NAME_rights;
	static const std::string FIELD_NAME_series;
	static const std::string FIELD_NAME_series_index;
	static const std::string FIELD_NAME_size;
	static const std::string FIELD_NAME_tags;
	static const std::string FIELD_NAME_thumbnail_data;
	static const std::string FIELD_NAME_thumbnail_height;
	static const std::string FIELD_NAME_thumbnail_width;
	static const std::string FIELD_NAME_timestamp;
	static const std::string FIELD_NAME_title_sort;
	static const std::string FIELD_NAME_uuid;
	static const std::string LOG_TAG;


public:
    /** Default constructor */
	EBook();
    /** Default destructor */
	~EBook();

    unsigned int getApplicationId() const;
    std::string getAuthorSort() const;
    std::string getAuthorString() const;
    std::string getBookProducer() const;
    std::string getCategory() const;
    std::string getComments() const;
    std::string getCover() const;
    std::string getDbId() const;
    std::string getDdc() const;
    unsigned int getId() const;
    std::string getIsbn() const;
    std::string getLanguage() const;
    std::string getLcc() const;
    std::string getLccn() const;
    std::string getLpath() const;
    std::string getMime() const;
    std::string getPubdate() const;
    std::string getPublicationType() const;
    std::string getPublisher() const;
    unsigned int getRating() const;
    std::string getRights() const;
    std::string getSeries() const;
    std::string getSeriesIndex() const;
    unsigned int getSize() const;
    std::string getTagString() const;
    std::string getThumbnailData() const;
    unsigned int getThumbnailHeight() const;
    unsigned int getThumbnailWidth() const;
    std::string getTimestamp() const;
    std::string getTitle() const;
    std::string getTitleSort() const;
    std::string getUuid() const;
    void setApplicationId(unsigned int applicationId);
    void setAuthorSort(std::string authorSort);
    void setAuthorString(std::string authorString);
    void setBookProducer(std::string bookProducer);
    void setCategory(std::string category);
    void setComments(std::string comments);
    void setCover(std::string cover);
    void setDbId(std::string dbId);
    void setDdc(std::string ddc);
    void setId(unsigned int id);
    void setIsbn(std::string isbn);
    void setLanguage(std::string language);
    void setLcc(std::string lcc);
    void setLccn(std::string lccn);
    void setLpath(std::string lpath);
    void setMime(std::string mime);
    void setPubdate(std::string pubdate);
    void setPublicationType(std::string publicationType);
    void setPublisher(std::string publisher);
    void setRating(unsigned int rating);
    void setRights(std::string rights);
    void setSeries(std::string series);
    void setSeriesIndex(std::string seriesIndex);
    void setSize(unsigned int size);
    void setTagString(std::string tagString);
    void setThumbnailData(std::string thumbnailData);
    void setThumbnailHeight(unsigned int thumbnailHeight);
    void setThumbnailWidth(unsigned int thumbnailWidth);
    void setTimestamp(std::string timestamp);
    void setTitle(std::string title);
    void setTitleSort(std::string titleSort);
    void setUuid(std::string uuid);


private:
	unsigned int m_application_id;
	std::string m_author_sort;
	//FIXME:    HashMap<std::string, std::string> m_author_sort_map;
	std::string m_authorString;
	std::string m_book_producer;
	std::string m_category;
	//FIXME:    HashMap<std::string, std::string> m_classifiers;
	std::string m_comments;
	std::string m_cover;
	//FIXME:    List<std::string> m_cover_data;
	std::string m_db_id;
	std::string m_ddc;
	unsigned int m_id;
	std::string m_isbn;
	std::string m_language;
	//FIXME:    List<std::string> m_languages;
	std::string m_lcc;
	std::string m_lccn;
	std::string m_lpath;
	std::string m_mime;
	std::string m_pubdate;
	std::string m_publication_type;
	std::string m_publisher;
	unsigned int m_rating;
	std::string m_rights;
	std::string m_series;
	std::string m_series_index;
	unsigned int m_size;
	std::string m_tagString;
	unsigned int m_thumbnail_height;
	unsigned int m_thumbnail_width;
	std::string m_thumbnail_data;
	std::string m_timestamp;
	std::string m_title;
	std::string m_title_sort;
	//FIXME:    List<std::string> m_user_metadata;
	std::string m_uuid;

};

#endif
