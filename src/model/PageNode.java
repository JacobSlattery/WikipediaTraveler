package model;

public class PageNode implements Comparable<PageNode> {

	private int id;
	private String name;
	private Integer previousNodeID;
	private int hits;
	private int[] links;

	/**
	 * PageNode which holds an id and name. Ability to track number of
	 * websites that link to this node (hits), along with the ID of a previous node
	 * and the IDs of the nodes which this node links to.
	 * 
	 * @param id   the number id of this node
	 * @param name the name of this node
	 */
	public PageNode(int id, String name) {
		this.id = id;
		this.hits = 0;
		this.name = name;
		this.previousNodeID = null;
		this.links = new int[] {};
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int[] getLinks() {
		return this.links;
	}

	public void setLinks(int[] links) {
		this.links = links;
	}

	public void hit() {
		this.hits++;
	}

	public int getHits() {
		return this.hits;
	}

	public int getID() {
		return this.id;
	}

	public Integer getPrevious() {
		return this.previousNodeID;
	}

	public void setPrevious(Integer newPrevious) {
		if (newPrevious == null) {
			this.previousNodeID = newPrevious;
		} else if (!newPrevious.equals(this.id)) {
			this.previousNodeID = newPrevious;
		}

	}

	public int compareTo(PageNode other) {
		return other.getHits() - this.getHits();
	}

	@Override
	public boolean equals(Object other) {
		if (other == this)
			return true;
		try {
			PageNode otherNode = (PageNode) other;
			return (this.id == otherNode.getID());
		} catch (Exception e) {
			return false;
		}

	}

	@Override
	public int hashCode() {
		return this.id;
	}

}
