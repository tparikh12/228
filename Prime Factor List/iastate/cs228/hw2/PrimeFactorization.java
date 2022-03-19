package edu.iastate.cs228.hw2;

/**
 *  
 * @author Tanay Parikh 
 *
 */

import java.util.ListIterator;
import java.util.NoSuchElementException;

public class PrimeFactorization implements Iterable<PrimeFactor> {
	private static final long OVERFLOW = -1;
	private long value; // the factored integer
						// it is set to OVERFLOW when the number is greater than 2^63-1, the
						// largest number representable by the type long.

	/**
	 * Reference to dummy node at the head.
	 */
	private Node head;

	/**
	 * Reference to dummy node at the tail.
	 */
	private Node tail;

	private int size; // number of distinct prime factors

	// ------------
	// Constructors
	// ------------

	/**
	 * Default constructor constructs an empty list to represent the number 1.
	 * 
	 * Combined with the add() method, it can be used to create a prime
	 * factorization.
	 */
	public PrimeFactorization() {

		head = new Node();
		tail = new Node();
		head.next = tail;
		tail.previous = head;
		size = 0;

	}

	/**
	 * Obtains the prime factorization of n and creates a doubly linked list to
	 * store the result. Follows the direct search factorization algorithm in
	 * Section 1.2 of the project description.
	 * 
	 * @param n
	 * @throws IllegalArgumentException if n < 1
	 */
	public PrimeFactorization(long n) throws IllegalArgumentException {

		this();
		for (int i = 2; i <= n / i; i++) {
			while (n % i == 0) {
				add(i, 1);
				n = n / i;
			}
		}
		if (n > 1) {
			add((int) n, 1);
		}
		updateValue();

	}

	/**
	 * Copy constructor. It is unnecessary to verify the primality of the numbers in
	 * the list.
	 * 
	 * @param pf
	 */
	public PrimeFactorization(PrimeFactorization pf) {
		this(pf.value);
	}

	/**
	 * Constructs a factorization from an array of prime factors. Useful when the
	 * number is too large to be represented even as a long integer.
	 * 
	 * @param pflist
	 */
	public PrimeFactorization(PrimeFactor[] pfList) {
		this();
		for (PrimeFactor factor : pfList) {
			this.add(factor.prime, factor.multiplicity);
		}
	}

	// --------------
	// Primality Test
	// --------------

	/**
	 * Test if a number is a prime or not. Check iteratively from 2 to the largest
	 * integer not exceeding the square root of n to see if it divides n.
	 * 
	 * @param n
	 * @return true if n is a prime false otherwise
	 */
	public static boolean isPrime(long n) {
		for (long i = 2; i <= n / 2; i++) {
			if (n % i == 0) {
				return false;
			}
		}
		return true;
	}

	// ---------------------------
	// Multiplication and Division
	// ---------------------------

	/**
	 * Multiplies the integer v represented by this object with another number n.
	 * Note that v may be too large (in which case this.value == OVERFLOW). You can
	 * do this in one loop: Factor n and traverse the doubly linked list
	 * simultaneously. For details refer to Section 3.1 in the project description.
	 * Store the prime factorization of the product. Update value and size.
	 * 
	 * @param n
	 * @throws IllegalArgumentException if n < 1
	 */
	public void multiply(long n) throws IllegalArgumentException {

		if (n < 1) {
			throw new IllegalArgumentException();
		}
		multiply(new PrimeFactorization(n));

	}

	/**
	 * Multiplies the represented integer v with another number in the factorization
	 * form. Traverse both linked lists and store the result in this list object.
	 * See Section 3.1 in the project description for details of algorithm.
	 * 
	 * @param pf
	 */
	public void multiply(PrimeFactorization pf) {

		PrimeFactorizationIterator facIterator = pf.iterator();
		PrimeFactorizationIterator iterator = this.iterator();
		while (facIterator.hasNext()) {

			PrimeFactor prime = facIterator.next();
			if (iterator.hasNext()) {
				boolean added = false;
				while (iterator.hasNext()) {
					PrimeFactor next = iterator.next();
					if (next.prime >= prime.prime) {
						iterator.previous();
						iterator.add(prime);
						added = true;
						break;
					}
				}
				if (!added) {
					add(prime.prime, prime.multiplicity);
				}
			} else {
				add(prime.prime, prime.multiplicity);
			}
			facIterator.remove();

		}
		updateValue();
		
	}

	/**
	 * Multiplies the integers represented by two PrimeFactorization objects.
	 * 
	 * @param pf1
	 * @param pf2
	 * @return object of PrimeFactorization to represent the product
	 */
	public static PrimeFactorization multiply(PrimeFactorization pf1, PrimeFactorization pf2) {
		
		PrimeFactorization newNumber = new PrimeFactorization(pf1);
		newNumber.multiply(pf2);
		return newNumber;
		
	}

	/**
	 * Divides the represented integer v by n. Make updates to the list, value, size
	 * if divisible. No update otherwise. Refer to Section 3.2 in the project
	 * description for details.
	 * 
	 * @param n
	 * @return true if divisible false if not divisible
	 * @throws IllegalArgumentException if n <= 0
	 */
	public boolean dividedBy(long n) throws IllegalArgumentException {

		if (this.value != -1 && this.value < n) {
			return false;
		}
		if (this.value == n) {
			clearList();
			this.value = 1;
			return true;
		} else {
			return dividedBy(new PrimeFactorization(n));
		}

	}

	/**
	 * Division where the divisor is represented in the factorization form. Update
	 * the linked list of this object accordingly by removing those nodes housing
	 * prime factors that disappear after the division. No update if this number is
	 * not divisible by pf. Algorithm details are given in Section 3.2.
	 * 
	 * @param pf
	 * @return true if divisible by pf false otherwise
	 */
	public boolean dividedBy(PrimeFactorization pf) {

		if ((this.value != -1 && pf.value != -1 && this.value < pf.value) || (this.value != -1 && pf.value() == -1)) {
			return false;
		} else if (this.value == pf.value) {
			clearList();
			this.value = 1;
			return true;
		}
		PrimeFactorizationIterator iterPf = pf.iterator();
		PrimeFactorizationIterator iterCopy = this.iterator();
		while (iterPf.hasNext()) {
			while (iterCopy.hasNext()) {
				if ((iterCopy.cursor.pFactor.prime >= iterPf.cursor.pFactor.prime)) {
					break;
				}
				iterCopy.next();
			}
			if (iterCopy.cursor.pFactor.prime > iterPf.cursor.pFactor.prime
					|| (iterCopy.cursor.pFactor.prime == iterPf.cursor.pFactor.prime
							&& iterCopy.cursor.pFactor.multiplicity < iterPf.cursor.pFactor.multiplicity)) {
				return false;
			} else {
				int difference = Math.abs(iterCopy.cursor.pFactor.multiplicity - iterPf.cursor.pFactor.multiplicity);
				if (difference == 0) {
					if(iterCopy.hasNext()) {
						iterCopy.next();
						iterCopy.remove();
					} else {
						Node cursor = iterCopy.cursor.previous;
						cursor.next = tail;
						tail.previous = cursor;
					}
				} else {
					iterCopy.cursor.pFactor.multiplicity = difference;
				}
				iterPf.next();
			}
		}
		updateValue();
		return true;

	}

	/**
	 * Divide the integer represented by the object pf1 by that represented by the
	 * object pf2. Return a new object representing the quotient if divisible. Do
	 * not make changes to pf1 and pf2. No update if the first number is not
	 * divisible by the second one.
	 * 
	 * @param pf1
	 * @param pf2
	 * @return quotient as a new PrimeFactorization object if divisible null
	 *         otherwise
	 */
	public static PrimeFactorization dividedBy(PrimeFactorization pf1, PrimeFactorization pf2) {
		
		PrimeFactorization newNumber = new PrimeFactorization(pf1);
		if (newNumber.dividedBy(pf2)) {
			return newNumber;
		} else {
			return null;
		}
		
	}

	// -------------------------------------------------
	// Greatest Common Divisor and Least Common Multiple
	// -------------------------------------------------

	/**
	 * Computes the greatest common divisor (gcd) of the represented integer v and
	 * an input integer n. Returns the result as a PrimeFactor object. Calls the
	 * method Euclidean() if this.value != OVERFLOW.
	 * 
	 * It is more efficient to factorize the gcd than n, which can be much greater.
	 * 
	 * @param n
	 * @return prime factorization of gcd
	 * @throws IllegalArgumentException if n < 1
	 */
	public PrimeFactorization gcd(long n) throws IllegalArgumentException {

		if (n < 1) {
			throw new IllegalArgumentException();
		}
		if (this.value != OVERFLOW) {
			return new PrimeFactorization(Euclidean(this.value, n));
		}
		return gcd(new PrimeFactorization(n));

	}

	/**
	 * Implements the Euclidean algorithm to compute the gcd of two natural numbers
	 * m and n. The algorithm is described in Section 4.1 of the project
	 * description.
	 * 
	 * @param m
	 * @param n
	 * @return gcd of m and n.
	 * @throws IllegalArgumentException if m < 1 or n < 1
	 */
	public static long Euclidean(long m, long n) throws IllegalArgumentException {

		if (m < 1 || n < 1) {
			throw new IllegalArgumentException();
		}
		while (n != 0) {
			long current = n;
			n = m % n;
			m = current;
		}
		return m;

	}

	/**
	 * Computes the gcd of the values represented by this object and pf by
	 * traversing the two lists. No direct computation involving value and pf.value.
	 * Refer to Section 4.2 in the project description on how to proceed.
	 * 
	 * @param pf
	 * @return prime factorization of the gcd
	 */
	public PrimeFactorization gcd(PrimeFactorization pf) {

		PrimeFactorization gcd = new PrimeFactorization();
		PrimeFactorizationIterator iterPf = pf.iterator();
		PrimeFactorizationIterator iterCopy = this.iterator();
		while (iterPf.hasNext() && iterPf.cursor.pFactor != null) {
			while (iterCopy.hasNext() && iterCopy.cursor.pFactor != null) {
				if ((iterCopy.cursor.pFactor.prime >= iterPf.cursor.pFactor.prime)) {
					break;
				}
				iterCopy.next();
			}
			if (iterCopy.cursor.pFactor != null && (iterCopy.cursor.pFactor.prime == iterPf.cursor.pFactor.prime)) {

				gcd.add(iterCopy.cursor.pFactor.prime,
						Math.min(iterCopy.cursor.pFactor.multiplicity, iterPf.cursor.pFactor.multiplicity));

			}
			iterPf.next();

		}
		return gcd;

	}

	/**
	 * 
	 * @param pf1
	 * @param pf2
	 * @return prime factorization of the gcd of two numbers represented by pf1 and
	 *         pf2
	 */
	public static PrimeFactorization gcd(PrimeFactorization pf1, PrimeFactorization pf2) {
		
		PrimeFactorization newNumber = new PrimeFactorization(pf1);
		return newNumber.gcd(pf2);
		
	}

	/**
	 * Computes the least common multiple (lcm) of the two integers represented by
	 * this object and pf. The list-based algorithm is given in Section 4.3 in the
	 * project description.
	 * 
	 * @param pf
	 * @return factored least common multiple
	 */
	public PrimeFactorization lcm(PrimeFactorization pf) {

		PrimeFactorization lcm = new PrimeFactorization();
		PrimeFactorizationIterator iterPf = pf.iterator();
		PrimeFactorizationIterator iterCopy = this.iterator();
		while (iterPf.hasNext() && iterPf.cursor.pFactor != null) {

			if (iterCopy.hasNext() && iterCopy.cursor.pFactor != null) {

				if (iterCopy.cursor.pFactor.prime == iterPf.cursor.pFactor.prime) {
					lcm.add(iterCopy.cursor.pFactor.prime,
							Math.max(iterCopy.cursor.pFactor.multiplicity, iterPf.cursor.pFactor.multiplicity));
					iterPf.next();
					iterCopy.next();
				} else {

					if (iterCopy.cursor.pFactor.prime > iterPf.cursor.pFactor.prime) {

						while (iterPf.hasNext() && iterPf.cursor.pFactor != null) {
							if ((iterCopy.cursor.pFactor.prime > iterPf.cursor.pFactor.prime)) {
								lcm.add(iterPf.cursor.pFactor.prime, iterPf.cursor.pFactor.multiplicity);
								iterPf.next();
							} else {
								break;
							}
						}

					} else {

						while (iterCopy.hasNext() && iterCopy.cursor.pFactor != null) {
							if ((iterCopy.cursor.pFactor.prime < iterPf.cursor.pFactor.prime)) {
								lcm.add(iterCopy.cursor.pFactor.prime, iterCopy.cursor.pFactor.multiplicity);
								iterCopy.next();
							} else {
								break;
							}
						}

					}

				}

			} else {
				break;
			}

		}
		while (iterCopy.hasNext() && iterCopy.cursor.pFactor != null) {
			if (iterCopy.cursor.pFactor != null) {
				lcm.add(iterCopy.cursor.pFactor.prime, iterCopy.cursor.pFactor.multiplicity);
				iterCopy.next();
			} else {
				break;
			}
		}
		while (iterPf.hasNext() && iterPf.cursor.pFactor != null) {
			if (iterPf.cursor.pFactor != null) {
				lcm.add(iterPf.cursor.pFactor.prime, iterPf.cursor.pFactor.multiplicity);
				iterPf.next();
			} else {
				break;
			}
		}
		return lcm;

	}

	/**
	 * Computes the least common multiple of the represented integer v and an
	 * integer n. Construct a PrimeFactors object using n and then call the lcm()
	 * method above. Calls the first lcm() method.
	 * 
	 * @param n
	 * @return factored least common multiple
	 * @throws IllegalArgumentException if n < 1
	 */
	public PrimeFactorization lcm(long n) throws IllegalArgumentException {
		
		if (n < 1) {
			throw new IllegalArgumentException();
		}
		return lcm(new PrimeFactorization(n));
		
	}

	/**
	 * Computes the least common multiple of the integers represented by pf1 and
	 * pf2.
	 * 
	 * @param pf1
	 * @param pf2
	 * @return prime factorization of the lcm of two numbers represented by pf1 and
	 *         pf2
	 */
	public static PrimeFactorization lcm(PrimeFactorization pf1, PrimeFactorization pf2) {
		
		PrimeFactorization newNumber = new PrimeFactorization(pf1);
		newNumber.lcm(pf2);
		return newNumber;
		
	}

	// ------------
	// List Methods
	// ------------

	/**
	 * Traverses the list to determine if p is a prime factor.
	 * 
	 * Precondition: p is a prime.
	 * 
	 * @param p
	 * @return true if p is a prime factor of the number v represented by this
	 *         linked list false otherwise
	 * @throws IllegalArgumentException if p is not a prime
	 */
	public boolean containsPrimeFactor(int p) throws IllegalArgumentException {
		
		if (isPrime(p)) {
			Node current = head;
			while (current != null && current.pFactor != null) {
				if (current.pFactor.prime == p) {
					return true;
				}
				current = current.next;
			}
			return false;

		} else {
			throw new IllegalArgumentException();
		}
		
	}

	// The next two methods ought to be private but are made public for testing
	// purpose. Keep
	// them public

	/**
	 * Adds a prime factor p of multiplicity m. Search for p in the linked list. If
	 * p is found at a node N, add m to N.multiplicity. Otherwise, create a new node
	 * to store p and m.
	 * 
	 * Precondition: p is a prime.
	 * 
	 * @param p prime
	 * @param m multiplicity
	 * @return true if m >= 1 false if m < 1
	 */
	public boolean add(int p, int m) {
		
		if (m < 1) {
			return false;
		} else {
			Node current = findNode(p);
			if (current == null) {
				Node newNode = new Node(p, m);
				Node last = tail.previous;
				newNode.next = tail;
				newNode.previous = last;
				tail.previous = newNode;
				last.next = newNode;
				size++;
			} else {
				current.pFactor.multiplicity += m;
			}
			updateValue();
			return true;

		}
	}
	
	/**
	 * Removes m from the multiplicity of a prime p on the linked list. It starts by
	 * searching for p. Returns false if p is not found, and true if p is found. In
	 * the latter case, let N be the node that stores p. If N.multiplicity > m,
	 * subtracts m from N.multiplicity. If N.multiplicity <= m, removes the node N.
	 * 
	 * Precondition: p is a prime.
	 * 
	 * @param p
	 * @param m
	 * @return true when p is found. false when p is not found.
	 * @throws IllegalArgumentException if m < 1
	 */
	public boolean remove(int p, int m) throws IllegalArgumentException {
		
		if (m < 1) {
			throw new IllegalArgumentException();
		} else {
			Node current = findNode(p);
			if (current == null) {
				return false;
			} else {
				if (current.pFactor.multiplicity > m) {
					current.pFactor.multiplicity -= m;
				} else {
					int index = findNodeIndex(p);
					if(index != -1) {
						remove(index);
						size--;
					}
				}
			}
			updateValue();
			return true;

		}
	}

	/**
	 * 
	 * @return size of the list
	 */
	public int size() {
		return size;
	}

	/**
	 * Writes out the list as a factorization in the form of a product. Represents
	 * exponentiation by a caret. For example, if the number is 5814, the returned
	 * string would be printed out as "2 * 3^2 * 17 * 19".
	 */
	@Override
	public String toString() {
		if (size == 0) {
			return "1";
		} else {
			String data = "";
			Node current = head;
			while (current != null) {
				if (current.pFactor != null)
					data += current.toString() + " * ";
				current = current.next;
			}
			if (data.length() >= 3) {
				data = data.substring(0, data.length() - 3);
			}
			return data.trim();
		}
	}

	// The next three methods are for testing, but you may use them as you like.

	/**
	 * @return true if this PrimeFactorization is representing a value that is too
	 *         large to be within long's range. e.g. 999^999. false otherwise.
	 */
	public boolean valueOverflow() {
		return value == OVERFLOW;
	}

	/**
	 * @return value represented by this PrimeFactorization, or -1 if
	 *         valueOverflow()
	 */
	public long value() {
		return value;
	}

	public PrimeFactor[] toArray() {
		PrimeFactor[] arr = new PrimeFactor[size];
		int i = 0;
		for (PrimeFactor pf : this)
			arr[i++] = pf;
		return arr;
	}

	@Override
	public PrimeFactorizationIterator iterator() {
		return new PrimeFactorizationIterator();
	}

	/**
	 * Doubly-linked node type for this class.
	 */
	private class Node {
		public PrimeFactor pFactor; // prime factor
		public Node next;
		public Node previous;

		/**
		 * Default constructor for creating a dummy node.
		 */
		public Node() {

			pFactor = null;
			next = null;
			previous = null;

		}

		/**
		 * Precondition: p is a prime
		 * 
		 * @param p prime number
		 * @param m multiplicity
		 * @throws IllegalArgumentException if m < 1
		 */
		public Node(int p, int m) throws IllegalArgumentException {
			this(new PrimeFactor(p, m));
		}

		/**
		 * Constructs a node over a provided PrimeFactor object.
		 * 
		 * @param pf
		 * @throws IllegalArgumentException
		 */
		public Node(PrimeFactor pf) {
			this.pFactor = pf;
		}

		/**
		 * Printed out in the form: prime + "^" + multiplicity. For instance "2^3".
		 * Also, deal with the case pFactor == null in which a string "dummy" is
		 * returned instead.
		 */
		@Override
		public String toString() {
			if (pFactor == null)
				return "dummy";
			else
				return pFactor.toString();
		}
	}

	private class PrimeFactorizationIterator implements ListIterator<PrimeFactor> {
		// Class invariants:
		// 1) logical cursor position is always between cursor.previous and cursor
		// 2) after a call to next(), cursor.previous refers to the node just returned
		// 3) after a call to previous() cursor refers to the node just returned
		// 4) index is always the logical index of node pointed to by cursor

		private Node cursor = head.next;
		private Node pending = null; // node pending for removal
		private int index = 0;
		private int size = size();

		// other instance variables ...

		/**
		 * Default constructor positions the cursor before the smallest prime factor.
		 */
		public PrimeFactorizationIterator() {

		}

		@Override
		public boolean hasNext() {
			return index < size;
		}

		@Override
		public boolean hasPrevious() {
			return index > 0;
		}

		@Override
		public PrimeFactor next() {
			if (!hasNext())
				throw new NoSuchElementException();
			pending = cursor;
			PrimeFactor item = cursor.pFactor;
			cursor = cursor.next;
			index++;
			return item;
		}

		@Override
		public PrimeFactor previous() {

			if (!hasPrevious())
				throw new NoSuchElementException();
			cursor = cursor.previous;
			index--;
			pending = cursor;
			return cursor.pFactor;

		}

		/**
		 * Removes the prime factor returned by next() or previous()
		 * 
		 * @throws IllegalStateException if pending == null
		 */
		@Override
		public void remove() throws IllegalStateException {

			if (pending != null) {

				Node previous = pending.previous;
				Node current = pending.next;
				previous.next = current;
				current.previous = previous;
				size--;
				if (cursor == pending)
					cursor = current;
				else
					index--;
				pending = null;

			} else {
				throw new IllegalStateException();
			}
		}

		/**
		 * Adds a prime factor at the cursor position. The cursor is at a wrong position
		 * in either of the two situations below:
		 * 
		 * a) pf.prime < cursor.previous.pFactor.prime if cursor.previous != head. b)
		 * pf.prime > cursor.pFactor.prime if cursor != tail.
		 * 
		 * Take into account the possibility that pf.prime == cursor.pFactor.prime.
		 * 
		 * Precondition: pf.prime is a prime.
		 * 
		 * @param pf
		 * @throws IllegalArgumentException if the cursor is at a wrong position.
		 */
		@Override
		public void add(PrimeFactor pf) throws IllegalArgumentException {
			if ((cursor.previous != head && pf.prime < cursor.previous.pFactor.prime)
					|| (cursor != tail && pf.prime > cursor.pFactor.prime)) {
				throw new IllegalArgumentException();
			} else {
				if (cursor.pFactor.prime == pf.prime) {
					cursor.pFactor.multiplicity += pf.multiplicity;
				} else {
					Node newNode = new Node(pf.prime, pf.multiplicity);
					Node previous = cursor.previous;
					Node current = cursor;
					newNode.pFactor = pf;
					previous.next = newNode;
					newNode.next = current;
					current.previous = newNode;
					newNode.previous = previous;
					size++;
					index++;
				}
			}
		}

		@Override
		public int nextIndex() {
			return index;
		}

		@Override
		public int previousIndex() {
			return index - 1;
		}

		@Deprecated
		@Override
		public void set(PrimeFactor pf) {
			throw new UnsupportedOperationException(getClass().getSimpleName() + " does not support set method");
		}

		// Other methods you may want to add or override that could possibly facilitate
		// other operations, for instance, addition, access to the previous element,
		// etc.
		//
		// ...
		//
	}

	// --------------
	// Helper methods
	// --------------

	/**
	 * Inserts toAdd into the list after current without updating size.
	 * 
	 * Precondition: current != null, toAdd != null
	 */
	private void link(Node current, Node toAdd) {
		Node last = tail.previous;
		toAdd.next = tail;
		toAdd.previous = last;
		tail.previous = toAdd;
		last.next = toAdd;
	}

	/**
	 * Removes toRemove from the list without updating size.
	 */
	private void unlink(Node toRemove) {
		// TODO
	}

	/**
	 * Remove all the nodes in the linked list except the two dummy nodes.
	 * 
	 * Made public for testing purpose. Ought to be private otherwise.
	 */
	public void clearList() {

		head.next = tail;
		tail.previous = head;
		size = 0;

	}
	
public Node removeFront() {
		
		Node current = head;
		if (head.next == null) {
			tail = null;
		} else {
			head.next.previous = null;
		}
		head = head.next;
		size--;
		return current;
	
	}

	public Node removeEnd() {
		
		Node current = tail;
		if (head.next == null) {
			head = null;
		} else {
			tail.previous.next = null;
		}
		tail = tail.previous;
		size--;
		return current;
		
	}

	public Node remove(int index) {
		
		index = index + 1;
		Node current = head;
		if (index == 0) {
			return removeFront();
		} else if (index == size - 1) {
			return removeEnd();
		} else {
			
			for (int j = 0; j < index && current.next != null; j++) {
				current = current.next;
			}
			current.previous.next = current.next;
			current.next.previous = current.previous;
			
		}
		return current;
		
	}

	private Node findNode(int p) {

		Node current = head;
		while (current != null) {
			if (current.pFactor != null && current.pFactor.prime == p) {
				return current;
			}
			current = current.next;
		}
		return null;

	}

	private int findNodeIndex(int p) {

		int index = -1;
		Node current = head;
		while (current != null) {
			if (current.pFactor != null && current.pFactor.prime == p) {
				return index;
			}
			index++;
			current = current.next;
		}
		return -1;

	}

	/**
	 * Multiply the prime factors (with multiplicities) out to obtain the
	 * represented integer. Use Math.multiply(). If an exception is throw, assign
	 * OVERFLOW to the instance variable value. Otherwise, assign the multiplication
	 * result to the variable.
	 * 
	 */
	private void updateValue() {
		
		try {
			
			this.value = 1;
			Node current = head;
			while (current != null) {
				if (current.pFactor != null) {
					this.value = Math.multiplyExact(value, (long) Math.pow(current.pFactor.prime, current.pFactor.multiplicity));
				}
				current = current.next;
			}
			
		}
		catch (ArithmeticException e) {
			value = OVERFLOW;
		}
	}
}
