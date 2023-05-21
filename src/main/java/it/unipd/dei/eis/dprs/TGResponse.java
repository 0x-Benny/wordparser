package it.unipd.dei.eis.dprs;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

//@JsonIgnoreProperties(ignoreUnknown = true)
public class TGResponse {

    private String status;
    private String userTier;
    private int total;
    private int startIndex;
    private int pageSize;
    private int currentPage;
    private int pages;
    private String orderBy;
    private TGArticle[] results;

    public TGResponse() {
    }

    public TGResponse(final String status, final String userTier, final int total,
                    final int startIndex, final int pageSize, final int currentPage,
                    final int pages, final String orderBy, final TGArticle[] results) {
        this.status = status;
        this.userTier = userTier;
        this.total = total;
        this.startIndex = startIndex;
        this.pageSize = pageSize;
        this.currentPage = currentPage;
        this.pages = pages;
        this.orderBy = orderBy;
        this.results = results;
    }

    public String getStatus() {
        return status;
    }

    public String getUserTier() {
        return userTier;
    }

    public int getTotal() {
        return total;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public int getPages() {
        return pages;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public TGArticle[] getResults() {
        return results;
    }

    @Override
    public String toString() {
        return "test.";
    }
}

