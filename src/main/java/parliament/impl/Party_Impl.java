package parliament.impl;

import parliament.Party;
import parliament.Speaker;

import java.util.HashSet;
import java.util.Set;

/**
 * implementation of the party interface,
 * represents a party that is represented in the Bundestag
 *
 * @author Sophie Kaiser
 */
public class Party_Impl implements Party {

    /**
     * name of the party
     */
    private String name;

    /**
     * all members of the party
     */
    private Set<Speaker> members = new HashSet<>();

    /**
     * package private constructor to ensure Party_Impl can only be
     * instantiated via ParliamentFactory_Impl
     *
     * @param name name of the party
     * @author Sophie Kaiser
     */
    Party_Impl(String name) {
        this.name = name;
    }

    /**
     * get the name of a party
     *
     * @return the name of the party
     * @author Sophie Kaiser
     */
    @Override
    public String getName() {
        return this.name;
    }

    /**
     * set the name of a party
     *
     * @param name set the name of the party
     * @author Sophie Kaiser
     */
    @Override
    public void setName(String name) {
        this.name = name;
    }

    /**
     * get all members of a party
     *
     * @return all members of the party
     * @author Sophie Kaiser
     */
    @Override
    public Set<Speaker> getMembers() {
        return this.members;
    }

    /**
     * add a speaker to a party as a member
     *
     * @param member speaker to be added to the party as a member;
     *               this speaker should be a member of the Bundestag
     * @author Sophie Kaiser
     */
    @Override
    public void addMember(Speaker member) {
        // check if speaker is a member of the Bundestag
        if (member.isMember()) {
            this.members.add(member);
        } else {
            throw new IllegalArgumentException("Speakers that are not " +
                    "members of the Bundestag cannot be added to a party, " +
                    "grouping or group.");
        }
    }

    /**
     * remove a speaker from a party as a member
     *
     * @param member speaker to be removed from the party as a member
     * @author Sophie Kaiser
     */
    @Override
    public void removeMember(Speaker member) {
        this.members.remove(member);
    }
}
