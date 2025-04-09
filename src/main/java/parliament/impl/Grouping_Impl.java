package parliament.impl;

import parliament.Grouping;

/**
 * implementation of the Grouping interface,
 * represents a grouping or a group in the Bundestag
 * @author Sophie Kaiser
 */
public class Grouping_Impl extends Party_Impl implements Grouping {

    /**
     * the status of a grouping in the Bundestag;
     * groups have more extensive rights that groupings;
     * true if the grouping has group status in the Bundestag,
     * false if not
     */
    private boolean group;

    /**
     * package private constructor to ensure Grouping_Impl can only be
     * instantiated via ParliamentFactory_Impl
     * @param name name of the grouping or group
     * @param group true if the grouping has group status in the Bundestag,
     *              false if not
     * @author Sophie Kaiser
     */
    Grouping_Impl(String name, boolean group) {
        super(name);
        this.group = group;
    }

    /**
     * get the status of a grouping in the Bundestag;
     * groups have more extensive rights that groupings
     *
     * @return true if the grouping has group status in the Bundestag,
     * false if not
     * @author Sophie Kaiser
     */
    @Override
    public boolean isGroup() {
        return this.group;
    }

    /**
     * set the group status of a grouping in the Bundestag;
     * it is possible for this status to change within a legislative period
     *
     * @param group true if the grouping has group status,
     *              false if not
     * @author Sophie Kaiser
     */
    @Override
    public void setGroupStatus(boolean group) {
        this.group = group;
    }
}
