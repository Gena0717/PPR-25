package parliament;

/**
 * represents a grouping or a group in the Bundestag
 * @author Sophie Kaiser
 */
public interface Grouping extends Party {

    /**
     * get the status of a grouping in the Bundestag;
     * groups have more extensive rights that groupings
     * @return true if the grouping has group status in the Bundestag,
     * false if not
     * @author Sophie Kaiser
     */
    boolean isGroup();

    /**
     * set the group status of a grouping in the Bundestag;
     * it is possible for this status to change within a legislative period
     * @param group true if the grouping has group status,
     *              false if not
     * @author Sophie Kaiser
     */
    void setGroupStatus(boolean group);
}
