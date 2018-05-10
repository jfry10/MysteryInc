/**
 * 
 */
package clueless;

/**
 * @author joshfry
 *
 */
abstract public class Card implements Comparable<Card> {
	public int cardType;

	abstract public void setName(String string);
	abstract public String getName();
	
	@Override
	public int compareTo(Card o) {
		if(this.cardType > o.cardType) {
			return 1;
		} else if (this.cardType < o.cardType) {
			return -1;
		} else {
			return this.getName().compareTo(o.getName());
		}
	}

	@Override
	public String toString() {
		return getName();
	}
	// Does the Card class actually do anything at all?
	// Adding words here so it's not completely lonely
}
