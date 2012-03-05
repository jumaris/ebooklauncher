#include <jni.h>
#include <string.h>
#include <android/log.h>

#include "ebook.h"
#include "sqlite3.h"
#include "sqlite3ext.h"

const std::string EBook::LOG_TAG("Calibre_EBook:");
const std::string EBook::DATABASE_TABLE_NAME("ebook");

const std::string EBook::FIELD_NAME_APPLICATION_ID("application_id");
const std::string EBook::FIELD_NAME_author_sort("author_sort");
const std::string EBook::FIELD_NAME_authors("authors");
const std::string EBook::FIELD_NAME_book_producer("book_producer");
const std::string EBook::FIELD_NAME_BOOK_TITLE("title");
const std::string EBook::FIELD_NAME_category("category");
const std::string EBook::FIELD_NAME_comments("comments");
const std::string EBook::FIELD_NAME_cover("cover");
const std::string EBook::FIELD_NAME_db_id("db_id");
const std::string EBook::FIELD_NAME_ddc("ddc");
const std::string EBook::FIELD_NAME_isbn("isbn");
const std::string EBook::FIELD_NAME_language("language");
const std::string EBook::FIELD_NAME_lcc("lcc");
const std::string EBook::FIELD_NAME_lccn("lccn");
const std::string EBook::FIELD_NAME_lpath("lpath");
const std::string EBook::FIELD_NAME_mime("mime");
const std::string EBook::FIELD_NAME_pubdate("pubdate");
const std::string EBook::FIELD_NAME_publication_type("publication_type");
const std::string EBook::FIELD_NAME_publisher("publisher");
const std::string EBook::FIELD_NAME_rating("rating");
const std::string EBook::FIELD_NAME_rights("rights");
const std::string EBook::FIELD_NAME_series("series");
const std::string EBook::FIELD_NAME_series_index("series_index");
const std::string EBook::FIELD_NAME_size("size");
const std::string EBook::FIELD_NAME_tags("tags");
const std::string EBook::FIELD_NAME_thumbnail_data("thumbnail_data");
const std::string EBook::FIELD_NAME_thumbnail_height("thumbnail_height");
const std::string EBook::FIELD_NAME_thumbnail_width("thumbnail_width");
const std::string EBook::FIELD_NAME_timestamp("timestamp");
const std::string EBook::FIELD_NAME_title_sort("title_sort");
const std::string EBook::FIELD_NAME_uuid("uuid");

EBook::EBook() {
}

EBook::~EBook() {
}


unsigned int EBook::getApplicationId() const
{
    return m_application_id;
}

std::string EBook::getAuthorSort() const
{
    return m_author_sort;
}

std::string EBook::getAuthorString() const
{
    return m_authorString;
}

std::string EBook::getBookProducer() const
{
    return m_book_producer;
}

std::string EBook::getCategory() const
{
    return m_category;
}

std::string EBook::getComments() const
{
    return m_comments;
}

std::string EBook::getCover() const
{
    return m_cover;
}

std::string EBook::getDbId() const
{
    return m_db_id;
}

std::string EBook::getDdc() const
{
    return m_ddc;
}

unsigned int EBook::getId() const
{
    return m_id;
}

std::string EBook::getIsbn() const
{
    return m_isbn;
}

std::string EBook::getLanguage() const
{
    return m_language;
}

std::string EBook::getLcc() const
{
    return m_lcc;
}

std::string EBook::getLccn() const
{
    return m_lccn;
}

std::string EBook::getLpath() const
{
    return m_lpath;
}

std::string EBook::getMime() const
{
    return m_mime;
}

std::string EBook::getPubdate() const
{
    return m_pubdate;
}

std::string EBook::getPublicationType() const
{
    return m_publication_type;
}

std::string EBook::getPublisher() const
{
    return m_publisher;
}

unsigned int EBook::getRating() const
{
    return m_rating;
}

std::string EBook::getRights() const
{
    return m_rights;
}

std::string EBook::getSeries() const
{
    return m_series;
}

std::string EBook::getSeriesIndex() const
{
    return m_series_index;
}

unsigned int EBook::getSize() const
{
    return m_size;
}

std::string EBook::getTagString() const
{
    return m_tagString;
}

std::string EBook::getThumbnailData() const
{
    return m_thumbnail_data;
}

unsigned int EBook::getThumbnailHeight() const
{
    return m_thumbnail_height;
}

unsigned int EBook::getThumbnailWidth() const
{
    return m_thumbnail_width;
}

std::string EBook::getTimestamp() const
{
    return m_timestamp;
}

std::string EBook::getTitle() const
{
    return m_title;
}

std::string EBook::getTitleSort() const
{
    return m_title_sort;
}

std::string EBook::getUuid() const
{
    return m_uuid;
}

void EBook::setApplicationId(unsigned int applicationId)
{
    m_application_id = applicationId;
}

void EBook::setAuthorSort(std::string authorSort)
{
    m_author_sort = authorSort;
}

void EBook::setAuthorString(std::string authorString)
{
    m_authorString = authorString;
}

void EBook::setBookProducer(std::string bookProducer)
{
    m_book_producer = bookProducer;
}

void EBook::setCategory(std::string category)
{
    m_category = category;
}

void EBook::setComments(std::string comments)
{
    m_comments = comments;
}

void EBook::setCover(std::string cover)
{
    m_cover = cover;
}

void EBook::setDbId(std::string dbId)
{
    m_db_id = dbId;
}

void EBook::setDdc(std::string ddc)
{
    m_ddc = ddc;
}

void EBook::setId(unsigned int id)
{
    m_id = id;
}

void EBook::setIsbn(std::string isbn)
{
    m_isbn = isbn;
}

void EBook::setLanguage(std::string language)
{
    m_language = language;
}

void EBook::setLcc(std::string lcc)
{
    m_lcc = lcc;
}

void EBook::setLccn(std::string lccn)
{
    m_lccn = lccn;
}

void EBook::setLpath(std::string lpath)
{
    m_lpath = lpath;
}

void EBook::setMime(std::string mime)
{
    m_mime = mime;
}

void EBook::setPubdate(std::string pubdate)
{
    m_pubdate = pubdate;
}

void EBook::setPublicationType(std::string publicationType)
{
    m_publication_type = publicationType;
}

void EBook::setPublisher(std::string publisher)
{
    m_publisher = publisher;
}

void EBook::setRating(unsigned int rating)
{
    m_rating = rating;
}

void EBook::setRights(std::string rights)
{
    m_rights = rights;
}

void EBook::setSeries(std::string series)
{
    m_series = series;
}

void EBook::setSeriesIndex(std::string seriesIndex)
{
    m_series_index = seriesIndex;
}

void EBook::setSize(unsigned int size)
{
    m_size = size;
}

void EBook::setTagString(std::string tagString)
{
    m_tagString = tagString;
}

void EBook::setThumbnailData(std::string thumbnailData)
{
    m_thumbnail_data = thumbnailData;
}

void EBook::setThumbnailHeight(unsigned int thumbnailHeight)
{
    m_thumbnail_height = thumbnailHeight;
}

void EBook::setThumbnailWidth(unsigned int thumbnailWidth)
{
    m_thumbnail_width = thumbnailWidth;
}

void EBook::setTimestamp(std::string timestamp)
{
    m_timestamp = timestamp;
}

void EBook::setTitle(std::string title)
{
    m_title = title;
}

void EBook::setTitleSort(std::string titleSort)
{
    m_title_sort = titleSort;
}

void EBook::setUuid(std::string uuid)
{
    m_uuid = uuid;
}

