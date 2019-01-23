package model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Graph {

	private static final int ASCII_ZERO = 48;
	private static final int TOP_VALUES_SIZE = 100;

	private static final String ID_FILENAME = "WikipediaIDs.txt";
	private static final String LINKS_FILENAME = "WikipediaLinks.txt";

	private HashMap<Integer, PageNode> graph;

	public Graph() {
		this.graph = new HashMap<Integer, PageNode>();

		this.populateGraph();
		this.readLinks();
	}

	public void mostLinkedTo() {

		ArrayList<PageNode> pageNodes = new ArrayList<PageNode>(this.graph.values());
		Collections.sort(pageNodes);
		int count = 1;
		int cap = TOP_VALUES_SIZE;
		if (this.graph.size() < TOP_VALUES_SIZE) {
			cap = this.graph.size();
		}
		for (int index = 0; index < cap; index++) {
			System.out.print("#" + count + "{" + pageNodes.get(index).getName() + " has "
					+ pageNodes.get(index).getHits() + " hits}\n");
			count++;
		}
	}

	public String search(String start, String end) {

		Integer startID = this.getIDWithName(start);
		Integer endID = this.getIDWithName(end);
		this.resetAllNodePevious();

		this.travelToEnd(startID, endID);

		String path = buildPath(startID, endID);
		return path;
	}

	public boolean isValidName(String name) {

		Integer key = this.getIDWithName(name);
		if (this.getIDWithName(name) != null && this.graph.containsKey(key)) {
			return true;
		}
		return false;

	}

	private void populateGraph() {

		FileReader reader = null;
		try {
			reader = new FileReader(new File(ID_FILENAME));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		BufferedReader bufferReader = new BufferedReader(reader);

		System.out.println("Populating Graph...");
		String line;
		try {
			while ((line = bufferReader.readLine()) != null) {
				this.loadIDAndNameFromLine(line);
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		try {
			reader.close();
			bufferReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void readLinks() {
		FileReader reader = null;
		try {
			reader = new FileReader(new File(LINKS_FILENAME));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		BufferedReader bufferReader = new BufferedReader(reader);

		System.out.println("Adding Links");
		int count = 1;
		String line;
		long start = System.currentTimeMillis();
		try {
			while ((line = bufferReader.readLine()) != null) {
				this.loadLinksFromLine(line);

				if (count % 100000 == 0) {
					System.out.println("Count: " + count / 1000 + "K" + "    Time: "
							+ (System.currentTimeMillis() - start) / 1000 + " seconds");
					start = System.currentTimeMillis();
				}
				count++;
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		try {
			reader.close();
			bufferReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void loadLinksFromLine(String line) {
		int spaceCount = 0;
		for (int i = 0; i < line.length(); i++) {
			if (line.charAt(i) == ' ') {
				spaceCount++;
			}
		}
		if (spaceCount == 0) {
			return;
		}

		int[] internalLinks = new int[spaceCount];
		int currentStep = 0;
		int sourceID = 0;
		while (currentStep < line.length() && line.charAt(currentStep) != ' ') {
			sourceID = 10 * sourceID + (line.charAt(currentStep) - ASCII_ZERO);
			currentStep++;
		}

		currentStep++;
		int index = 0;
		int internalLinkID = 0;
		for (; currentStep < line.length(); currentStep++) {
			if (line.charAt(currentStep) == ' ') {
				internalLinks[index++] = internalLinkID;
				this.graph.get(internalLinkID).hit();
				internalLinkID = 0;
			} else {
				internalLinkID = 10 * internalLinkID + (line.charAt(currentStep) - ASCII_ZERO);
			}
		}

		internalLinks[index] = internalLinkID;
		this.graph.get(sourceID).setLinks(internalLinks);
	}

	private void loadIDAndNameFromLine(String line) {
		int index = 0;
		while (index < line.length() && line.charAt(index) != ':') {
			index++;
		}

		int nameLength = index;
		char[] name = new char[nameLength];
		int id = 0;

		for (index = 0; index < nameLength; index++) {
			name[index] = line.charAt(index);
		}
		index++;
		for (; index < line.length(); index++) {
			id = 10 * id + (line.charAt(index) - ASCII_ZERO);
		}

		this.graph.put(id, new PageNode(id, new String(name)));

	}

	private boolean travelToEnd(Integer startID, Integer endID) {

		System.out.println("Traversing Nodes...");
		PageNode endNode = this.graph.get(endID);
		ArrayList<Integer> linksToSearch = new ArrayList<Integer>();
		ArrayList<Integer> visited = new ArrayList<Integer>();
		linksToSearch.add(startID);

		while (endNode.getPrevious() == null && linksToSearch.size() > 0) {
			ArrayList<Integer> currentDegreeLinks = new ArrayList<Integer>(linksToSearch);

			for (Integer currentID : currentDegreeLinks) {

				int[] currentNodeLinks = this.graph.get(currentID).getLinks();

				for (Integer linkID : currentNodeLinks) {
					PageNode linkNode = this.graph.get(linkID);

					if (linkNode.equals(endNode)) {
						endNode.setPrevious(currentID);
						return true;
					}
					if (linkNode.getPrevious() == null) {
						linkNode.setPrevious(currentID);
						if (!visited.contains(linkID)) {
							linksToSearch.add(linkID);
						}
					}

				}

				visited.add(currentID);
				linksToSearch.remove(currentID);

			}
		}

		return false;
	}

	private String buildPath(Integer startID, Integer endID) {

		System.out.println("Building Path...");
		ArrayList<Integer> trail = new ArrayList<Integer>();
		Integer current = endID;

		while (current != startID) {
			trail.add(current);
			current = this.graph.get(current).getPrevious();
		}
		trail.add(current);

		Collections.reverse(trail);
		String path = this.graph.get(trail.get(0)).getName();

		for (int index = 1; index < trail.size(); index++) {
			path += "=>" + this.graph.get(trail.get(index)).getName();
		}

		return path;
	}

	private Integer getIDWithName(String name) {

		for (PageNode pageNode : this.graph.values()) {
			if (pageNode.getName().equals(name)) {
				return pageNode.getID();
			}
		}
		return null;
	}

	private void resetAllNodePevious() {
		for (PageNode pageNode : this.graph.values()) {
			pageNode.setPrevious(null);
		}
	}

}
