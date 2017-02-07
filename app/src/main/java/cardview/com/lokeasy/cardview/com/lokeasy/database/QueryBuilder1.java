package cardview.com.lokeasy.cardview.com.lokeasy.database;

public class QueryBuilder1 {

    /*database name*/
    public String getDatabaseName() {
        return "code101";
    }

    /* mongolab API
     */
    public String getApiKey() {
        return "d48GVebVf7H_bJUJMgFmcX0GNONqy9dm";
    }

    /*
      URL that allows to manage the database,collections and documents

     */
    public String getBaseUrl()
    {
        return "https://api.mongolab.com/api/1/databases/"+getDatabaseName()+"/collections/";
    }

    /*
      Completes the formating of  URL and adds API key at the end
          */
    public String docApiKeyUrl()
    {
        return "?apiKey="+getApiKey();
    }

    /**
     * Get a specified document
     * @param docid
     * @return
     */
    public String docApiKeyUrl(String docid)
    {
        return "/"+docid+"?apiKey="+getApiKey();
    }

    /*
      Returns the docs101 collection

     */
    public String documentRequest()
    {
        return "doc2";
    }

    /*
      Builds a complete URL using the methods specified above

     */
    public String buildSupportsSaveURL()
    {
        return getBaseUrl()+documentRequest()+docApiKeyUrl();
    }


    public String buildSupportsGetURL()
    {
        return getBaseUrl()+documentRequest()+docApiKeyUrl();
    }

    /*
      Get a Mongodb document that corresponds to the given object id
     */
    public String buildSupportsUpdateURL(String doc_id)
    {
        return getBaseUrl()+documentRequest()+docApiKeyUrl(doc_id);
    }


    /*
      Formats the contact details for MongoHLab Posting


     */
    public String createSupport(MySupport support)
    {
        return String
                .format("{\"name\": \"%s\", "
                                + "\"email\": \"%s\", \"subject\": \"%s\", "
                                + "\"issue\": \"%s\"}",
                        support.name, support.email, support.subject,support.issue);
    }

    /*
      Update a given contact record


     */
    public String setSupportData(MySupport support) {
        return String.format("{ \"$set\" : "
                        + "{\"name\" : \"%s\", "
                        + "\"email\" : \"%s\", "
                        + "\"subject\" : \"%s\", "
                        + "\"issue\" : \"%s\" }" + "}",
                support.getName(),
                support.getEmail(), support.getSubject(),
                support.getIssue());
    }

}
