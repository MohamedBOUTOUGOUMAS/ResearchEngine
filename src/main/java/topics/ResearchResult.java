package topics;

public class ResearchResult {

    public Book book;
    public int nbMatched;
    public Float pageRank = 0F;
    public Float betweeness = 0F;

    public ResearchResult(Book book, int nbMatched) {
        this.book = book;
        this.nbMatched = nbMatched;
    }
}
