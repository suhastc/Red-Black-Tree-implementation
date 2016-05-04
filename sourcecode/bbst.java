
/****************************************************************/
/* Purpose: Implementation of event counter using Red Black Tree
 *          as part of the Programming Project
 * By: Suhas Tumkur Chandrashekhara (UFID: 49497535)
 * Term: Spring 2016
 * Subject: COP 5556 - Advanced Data Structures
 * Language used: JAVA
/****************************************************************/


import java.io.File;
import java.util.Scanner;


/****************************************************************/
/* Class: bbst
 * Purpose: Main class to do the entire processing starting from 
 *          reading the input from a text file to performing the
 *          entered operations on the built Red Black Tree  
/****************************************************************/
public class bbst 
{
	private final int RED   = 0;   //Assign 0 to RED
    private final int BLACK = 1;   //Assign 1 to BLACK

    /**********************************************************************/
    /* Class: Node
     * Purpose: To form a structure for the nodes that need to be 
     *          inserted into the Red Black Tree. It contains the key
     *          and its corresponding count. Along with this each node
     *          has 3 pointers pointing to its left child, right child 
     *          and parent. Also, every node maintains a color attribute
     *          to hold RED or BLACK value associated with it
    /**********************************************************************/
    private class Node 
    {
        int key = -1, count = -1, color = BLACK;    //(Key,Value) pair and color
        Node left = nil, right = nil, parent = nil; //Left & right child pointers and parent pointer

        //Constructor of class Node to initialize key and count values
        Node(int key, int count) 
        {
            this.key = key;
            this.count = count;
        } 
    }
        
    private final Node nil = new Node(-1, -1);  //Create a nil Node to initialize key and count to -1
    private Node root = nil;                    //Create and initialize root Node
    
    /**********************************************************************/
    /* Method: insert
     * Input parameters: a node of type Node
     * Return parameter: does not return anything (VOID)
     * Purpose: To insert a single new node that has been passed into 
     *          this method to the existing Red Black Tree. If the node 
     *          being inserted is not the root, then its color is set as 
     *          RED. Determines the position of insertion by comparing the 
     *          key value of the passed in node with the key values of the 
     *          existing nodes in the tree. The new node is always being
     *          inserted as a leaf node. Finally after insertion, fix the
     *          tree by calling insertFixUp method for the new node
    /**********************************************************************/
    private void insert(Node node) 
    {
        Node temp = root;
        if (root == nil)          //Tree is empty case
        {
            root = node;          //Make the new node as the root node
            node.color = BLACK;
            node.parent = nil;
        } 
        else 
        {
            node.color = RED;
            while (true) 
            {
                if (node.key < temp.key)      //Follow left subtree           
                {
                    if (temp.left == nil)     //Insert as left child
                    {
                        temp.left = node;
                        node.parent = temp;
                        break;
                    } 
                    else                         //Traverse left
                    {
                        temp = temp.left;
                    }
                } 
                else if (node.key >= temp.key)   //Follow right subtree    
                {
                    if (temp.right == nil)       //Insert as right child
                    {
                        temp.right = node;
                        node.parent = temp;
                        break;
                    } 
                    else 
                    {
                        temp = temp.right;       //Traverse right
                    }
                }
            }
            //Adjust the RedBlackTree after the insertion of the new node
            insertFixUp(node);
        }
    }

    /**********************************************************************/
    /* Method: insertFixUp
     * Input parameters: a node of type Node
     * Return parameter: does not return anything (VOID)
     * Purpose: To adjust the RedBlackTree after a node has been inserted. 
     *          This method further calls rotateLeft and rotateRight
     *          methods to adjust the RedBlackTree
    /**********************************************************************/
    private void insertFixUp(Node node) 
    {
        while (node.parent.color == RED) 
        {
            Node uncle = nil;
            if (node.parent == node.parent.parent.left)  //Means uncle is right child of grandparent
            {
                uncle = node.parent.parent.right;

                if (uncle != nil && uncle.color == RED) 
                {
                    node.parent.color = BLACK;       //Change parent's color to BLACK
                    uncle.color = BLACK;             //Change uncle's color to BLACK
                    node.parent.parent.color = RED;  //Change grandparent's color to RED
                    node = node.parent.parent;       //Make grandparent as the new node and repeat
                    continue;
                } 
                if (node == node.parent.right) 
                {
                    node = node.parent;   //Left rotate the parent
                    rotateLeft(node);
                } 
                node.parent.color = BLACK;        //Set parent's color to BLACK
                node.parent.parent.color = RED;   //Set grandparent's color to RED
                rotateRight(node.parent.parent);  //Right rotate the grandparent
            } 
            else        //Means uncle is left child of grandparent
            {
        		uncle = node.parent.parent.left;
                if (uncle != nil && uncle.color == RED)
                {	                	
                    node.parent.color = BLACK;        //Change parent's color to BLACK
                    uncle.color = BLACK;              //Change uncle's color to BLACK
                    node.parent.parent.color = RED;   //Change grandparent's color to RED
                    node = node.parent.parent;        //Make grandparent as the new node and repeat
                    continue;
                }
                if (node == node.parent.left) 
                {
                    node = node.parent;   //Right rotate the parent
                    rotateRight(node);
                }
                node.parent.color = BLACK;        //Set parent's color to BLACK
                node.parent.parent.color = RED;   //Set grandparent's color to RED
                rotateLeft(node.parent.parent);   //Left rotate the grandparent
            }
        }
        root.color = BLACK;      //Finally set the root's color to BLACK
    }

    
    /**********************************************************************/
    /* Method: rotateLeft
     * Input parameters: a node of type Node
     * Return parameter: does not return anything (VOID)
     * Purpose: To handle the left rotation of the tree to cope with the
     *          insertion/deletion of new/existing nodes respectively
    /**********************************************************************/
    void rotateLeft(Node node) 
    {
        if (node.parent != nil)     //Means not the root
        {
            if (node == node.parent.left)       //node is left child of its parent
            {
                node.parent.left = node.right;  //Assign right sub tree as parent's left sub tree
            } 
            else                                //node is right child of its parent
            {
                node.parent.right = node.right; //Assign right sub tree as parent's right sub tree
            }
            node.right.parent = node.parent;    
            node.parent = node.right;
            if (node.right.left != nil) 
            {
                node.right.left.parent = node;
            }
            node.right = node.right.left;
            node.parent.left = node;
        } 
        else     //Means given node is root and we need to rotate root
        {
            Node right = root.right;
            root.right = right.left;
            right.left.parent = root;   //Assign new root
            root.parent = right;
            right.left = root;
            right.parent = nil;
            root = right;              //Right child of old root becomes new root
        }
    }

    /**********************************************************************/
    /* Method: rotateRight
     * Input parameters: a node of type Node
     * Return parameter: does not return anything (VOID)
     * Purpose: To handle the left rotation of the tree to cope with the
     *          insertion/deletion of new/existing nodes respectively
    /**********************************************************************/
    void rotateRight(Node node) 
    {
        if (node.parent != nil)                 //Means not the root
        {
            if (node == node.parent.left)       //node is left child of its parent
            {
                node.parent.left = node.left;   //Assign left sub tree as parent's left sub tree
            } 
            else                                //node is right child of its parent
            {
                node.parent.right = node.left;  //Assign left sub tree as parent's right sub tree
            }

            node.left.parent = node.parent;
            node.parent = node.left;
            if (node.left.right != nil) 
            {
                node.left.right.parent = node;
            }
            node.left = node.left.right;
            node.parent.right = node;
        } 
        else      //Means given node is root and we need to rotate root
        {
            Node left = root.left;
            root.left = root.left.right;
            left.right.parent = root;         //Assign new root
            root.parent = left;
            left.right = root;
            left.parent = nil;
            root = left;                      //Left child of old root becomes new root
        }
    }
    
    /**********************************************************************/
    /* Method: findNode (Recursive)
     * Input parameters: a node of type Node
     * Return parameter: a node of type Node
     * Purpose: To search for a particular node based on the key value.
     *          Returns null if the key being looked for is not in the tree.
     *          Returns the searched for node if found
    /**********************************************************************/
    private Node findNode(int value, Node node) 
    {
        if (node == nil)      //Tree empty case, then return null
        {
            return null;
        }

        if (value < node.key)         //Must be present in the left subtree
        {
            if (node.left != nil) 
            {
                return findNode(value, node.left);   //Traverse left in search of the same key value
            }
            else
            	return null;
        } 
        else if (value > node.key)   //Must be present in the right subtree
        {
            if (node.right != nil) 
            {
                return findNode(value, node.right);  //Traverse right in search of the same key value
            }
            else
            	return null;
        } 
        else if (value == node.key)      //Match found
        {
            return node;                 //Return the matched node
        }
        return null;
    }
    
    /**********************************************************************/
    /* Method: delete 
     * Input parameters: a node of type Node
     * Return parameter: does not return anything (VOID)
     * Purpose: To delete a single new node that has been passed into 
     *          this method from the existing Red Black Tree. Handles 3 
     *          cases. Calls  replaceNodeWith method to actually take 
     *          out the node to be deleted and brings in the appropriate
     *          node to the deleted position. Finally after deletion, 
     *          fix the tree by calling deleteFixUp method for the 
     *          appropriate node.
    /**********************************************************************/
    void delete(Node z)
    {
        Node x;
        Node y = z;                      //Temporary reference y to the node to be deleted
        int y_original_color = y.color;
        
        if(z.left == nil)                //Node to be deleted has only right subtree
        {
            x = z.right;  
            replaceNodeWith(z, z.right); //Replace it with its right child
        }
        else if(z.right == nil)          //Node to be deleted has only left subtree
        {
            x = z.left;
            replaceNodeWith(z, z.left);  //Replace it with its left child
        }
        else                             //Node to be deleted has both left and right subtrees
        {
            y = treeMinimum(z.right);    //Find the minimum most element in the right subtree
            y_original_color = y.color;
            x = y.right;                 //To handle adjusting the tree after deletion
            if(y.parent == z)            //Node to be deleted has only one right child
                x.parent = y;
            else
            {
            	replaceNodeWith(y, y.right);    //Delete the minimum most node in right subtree
                y.right = z.right;
                y.right.parent = y;
            }
            replaceNodeWith(z, y);   //Replace node to be deleted with minimum node in right subtree
            y.left = z.left;
            y.left.parent = y;
            y.color = z.color; 
        }
        if(y_original_color == BLACK)  //If the deleted node was BLACK, then we need tree adjustment
            deleteFixUp(x);  
    }
    
    /**********************************************************************/
    /* Method: insertFixUp
     * Input parameters: 2 nodes of type Node. 1 to be deleted, 
     *                   other its replacement
     * Return parameter: does not return anything (VOID)
     * Purpose: To replace the node to be deleted with its correct 
     *          replacement node 
    /**********************************************************************/
    void replaceNodeWith(Node target, Node with)
    { 
        if(target.parent == nil)               //If root is being deleted
        {
            root = with;
        }
        else if(target == target.parent.left)  //If node being deleted is left child of its parent
        {
            target.parent.left = with;
        }
        else                                   //If node being deleted is right child of its parent
        {
            target.parent.right = with;
        }
        with.parent = target.parent;           //Set replacement node's parent pointer
    }
    
    /**********************************************************************/
    /* Method: treeMinimum
     * Input parameters: a node of type Node
     * Return parameter: a node of type Node - returns the minimum node
     * Purpose: To find the minimum most element in the subtree rooted at  
     *          the node that is being passed into this method. 
    /**********************************************************************/
    Node treeMinimum(Node subtree)
    {
        while(subtree.left != nil)             //Traverse left till we hit leftmost leaf
        {
            subtree = subtree.left;
        }
        return subtree;
    }
    
    /**********************************************************************/
    /* Method: treeMaximum
     * Input parameters: a node of type Node
     * Return parameter: a node of type Node - returns the maximum node
     * Purpose: To find the maximum most element in the subtree rooted at  
     *          the node that is being passed into this method. 
    /**********************************************************************/
    Node treeMaximum(Node subtree)
    {
        while(subtree.right != nil)             //Traverse right till we hit rightmost leaf
        {
            subtree = subtree.right;
        }
        return subtree;
    }
    
    /**********************************************************************/
    /* Method: deleteFixUp
     * Input parameters: a node of type Node
     * Return parameter: does not return anything (VOID)
     * Purpose: To adjust the RedBlackTree after a node has been deleted. 
     *          This method further calls rotateLeft and rotateRight
     *          methods to adjust the RedBlackTree
    /**********************************************************************/
    void deleteFixUp(Node x)
    {
        while(x != root && x.color == BLACK)
        { 
            if(x == x.parent.left)        //If the node is a left child of its parent
            {
                Node w = x.parent.right;  //Make w as x's sibling
                if(w.color == RED)
                {
                    w.color = BLACK;
                    x.parent.color = RED;
                    rotateLeft(x.parent);
                    w = x.parent.right;
                }
                if(w.left.color == BLACK && w.right.color == BLACK)
                {
                    w.color = RED;
                    x = x.parent;
                    continue;
                }
                else if(w.right.color == BLACK)
                {
                    w.left.color = BLACK;
                    w.color = RED;
                    rotateRight(w);
                    w = x.parent.right;
                }
                if(w.right.color == RED)
                {
                    w.color = x.parent.color;
                    x.parent.color = BLACK;
                    w.right.color = BLACK;
                    rotateLeft(x.parent);
                    x = root;
                }
            }
            else                            //If the node is a right child of its parent
            {
                Node w = x.parent.left;     //Make w as x's sibling
                if(w.color == RED)
                {
                    w.color = BLACK;
                    x.parent.color = RED;
                    rotateRight(x.parent);
                    w = x.parent.left;
                }
                if(w.right.color == BLACK && w.left.color == BLACK)
                {
                    w.color = RED;
                    x = x.parent;
                    continue;
                }
                else if(w.left.color == BLACK)
                {
                    w.right.color = BLACK;
                    w.color = RED;
                    rotateLeft(w);
                    w = x.parent.left;
                }
                if(w.left.color == RED)
                {
                    w.color = x.parent.color;
                    x.parent.color = BLACK;
                    w.left.color = BLACK;
                    rotateRight(x.parent);
                    x = root;
                }
            }
        }
        x.color = BLACK; 
    }
    
    /**********************************************************************/
    /* Method: successor (Recursive)
     * Input parameters: an integer value, a node of type Node
     * Return parameter: a node of type Node - returns the successor node 
     *                   if found, else returns null
     * Purpose: To get the successor node in the tree of the key value that 
     *          is passed in to this method. Search for the sent key value 
     *          starts from the root node which is passed in as the second 
     *          input parameter. 
    /**********************************************************************/
    public Node successor(int value, Node node)
    {
    	if (node == nil)      //Tree empty case, then return null
        {
            return null;
        }

        if (value < node.key)    
        {
            if (node.left != nil)   //Traverse left subtree if left subtree present
            {
                return successor(value, node.left);   //Traverse left in search of the same key value
            }
            else                    //No left subtree, then return the node itself
            {
            	return node;
            }
        } 
        else if (value > node.key)    
        {
            if (node.right != nil)   //Traverse right subtree if right subtree present
            {
                return successor(value, node.right);  //Traverse right in search of same key value
            }
            else                                     //Given node does not have right subtree
            {
            	Node temp = treeMaximum(root);
            	if (!(value > temp.key))
            	{
            		Node y = node.parent;                    
	                Node x = node;
	               //Traverse up the tree till we find a left link of the parent
	                while (y != null && x == y.right)     
	                {
	                    x = y;
	                    y = y.parent;
	                }
	               	return y;         	                
            	}
            	else
            	{
            		return null;
            	}
            }
        } 
        else if (value == node.key) 
        {
            if (node.right != nil)                   //Given node has right subtree
            {
            	return treeMinimum(node.right);            	
            }
            
            Node temp = treeMaximum(root);
            if (value != temp.key)
            {
            	Node y = node.parent;                    //Given node does not have right subtree
	            Node x = node;
	           //Traverse up the tree till we find a left link of the parent
	            while (y != null && x == y.right)     
	            {
	                x = y;
	                y = y.parent;
	            }
	           	return y;         	           
	        }
            else
            {
            	return null;
            }
        }
        return null;
    }
    
    /**********************************************************************/
    /* Method: predecessor (Recursive)
     * Input parameters: an integer value, a node of type Node
     * Return parameter: a node of type Node - returns the predecessor node 
     *                   if found, else returns null
     * Purpose: To get the predecessor node in the tree of the key value that 
     *          is passed in to this method. Search for the sent key value 
     *          starts from the root node which is passed in as the second 
     *          input parameter. 
    /**********************************************************************/
    public Node predecessor(int value, Node node)
    {
    	if (node == nil)      //Tree empty case, then return null
        {
            return null;
        }

        if (value < node.key) 
        {
            if (node.left != nil) 
            {
                return predecessor(value, node.left);   //Traverse left in search of same key value
            }
            else
            {            	
            	Node temp = treeMinimum(root);
            	if (!(value < temp.key))
            	{
	            	Node y = node.parent;                    
	                Node x = node;
	               //Traverse up the tree till we find a right link of the parent
	                while (y != null && x == y.left)     
	                {
	                    x = y;
	                    y = y.parent;
	                }
	               	return y;
            	}
            	else
            	{
            		return null;
            	}
            }
        } 
        else if (value > node.key)  
        {
            if (node.right != nil) 
            {
                return predecessor(value, node.right);  //Traverse right in search of same key value
            }
            else                                        
            {
            	return node;
            }
        } 
        else if (value == node.key) 
        {
            if (node.left != nil)                    //Given node has left subtree
            {
            	return treeMaximum(node.left);            	
            }
            
            Node temp = treeMinimum(root);
            if (value != temp.key)
            {
	            Node y = node.parent;                    //Given node does not have right subtree
	            Node x = node;
	           //Traverse up the tree till we find a right link of the parent
	            while (y != null && x == y.left)     
	            {
	                x = y;
	                y = y.parent;
	            }
	           	return y;
            }
            else
            {
            	return null;
            }
        }
        return null;
    }
    
    /***************************************************************************/
    /* Method: inRange (Recursive)
     * Input parameters: 2 integer values, a node of type Node
     * Return parameter: an integer which is the sum of all the counts of 
     *                   the keys which lie between the passed in 2 integer
     *                   values (range is inclusive)
     * Purpose: To sum up all the count values of all the keys which lie
     *          lie in between the lower and upper keys which form the range. 
    /***************************************************************************/
    int inRange(int id1, int id2, Node temp)
    {
    	int countedSum = 0;
    	if (temp != null)
    	{
	    	if ((id1 <= temp.key) && (id2 >= temp.key))               //node lies within the range
	    	{
	    		countedSum = temp.count + inRange(id1, id2, temp.left)  //add its count to the sum
	    		             + inRange(id1, id2, temp.right);
	    	}
	    	else if ((id1 <= temp.key) && (id2 < temp.key))  //node lies in the range partially
	    	{
	    		countedSum = inRange(id1, id2, temp.left);   //recursive call after moving left
	    	}
	    	else if ((id1 > temp.key) && (id2 >= temp.key))  //node lies in the range partially
	    	{
	    		countedSum = inRange(id1, id2, temp.right);  //recursive call after moving right
	    	}
    	}    	
    	return countedSum;   //return the final count sum
    }
    
    /***************************************************************************/
    /* Method: buildInitialRBT
     * Input parameters: a string parameter holding the filename to be read
     * Return parameter: nothing to be returned (VOID)
     * Purpose: To build the initial Red Black Tree tree by accessing the file
     *          that was passed into the program as a command line argument.
     *          Reads the file and forms arrays to hold key and count values.
     *          Calls the method calculateRedLevel to get the height of the 
     *          tree that will be constructed using the size of the tree. Then 
     *          calls the method arrayToRedBlackTreeRoot to actually construct 
     *          the tree using the arrays that were populated in this method 
    /***************************************************************************/
    public void buildInitialRBT(String filename)
    {
    	Scanner s = null;                       //To access input file for initial tree build
    	try
		{
			s = new Scanner(new File(filename));
		}
		catch(Exception e)
		{		
			   System.exit(0);		
		}

    	int length = s.nextInt();         //Get the number of entries in the input file		
    	int[] k = new int[length];        //Array to hold keys
    	int[] c = new int[length];        //Array to hold counts
    	int j = 0;
    	while (s.hasNext())               //Read from file till end of file is reached
		{
			k[j] = s.nextInt();           //Fill in array
			c[j] = s.nextInt();           //Fill in array
			j++;
		}    	
    	s.close();

    	int redLevel = calculateRedLevel(length);  //Obtain height of the tree 
    	root = arrayToRedBlackTreeRoot(k, c, 0, length-1, redLevel);  //Construct the tree
    }
    
    /***************************************************************************/
    /* Method: calculateRedLevel
     * Input parameters: an integer indicating the total nodes in tree
     * Return parameter: an integer indicating the height of the tree
     * Purpose: To calculate the height of the tree 
    /***************************************************************************/
    int calculateRedLevel(int size) 
    {
        double level = 0;
        int sz = size;
        while (sz > 0)
        {
        	level++;
        	sz = size;
        	sz = sz / ((int)Math.pow(2.0, level)) ;
        }
        int y = (int)level;
    	return y;
    }
    
    /***************************************************************************/
    /* Method: arrayToRedBlackTreeRoot (Recursive)
     * Input parameters: key and count arrays, low and high index of the tree,
     *                   and height of the tree. All are integer values
     * Return parameter: a node of type Node pointing to the root of the tree
     * Purpose: To build the initial tree in O(n) time using sorted input
     *          Builds a balanced binary tree and makes the color of all the 
     *          nodes in the last level of the tree to be RED to maintain the 
     *          Red Black Tree property
    /***************************************************************************/
    Node arrayToRedBlackTreeRoot(int[] keys, int[] counts, int i, int j, int redLevel) 
    {
        if (i > j)            //If lower index goes higher than last index
        {
            return nil;
        }
        
        int x = (j + i) / 2;  //find the middle element
        Node localRoot = new Node(keys[x], counts[x]);
        
        if (redLevel == 1)           //If on the last level of tree, color it RED
        	localRoot.color = RED;
        
        if (i <= j) 
        {
            localRoot.left = arrayToRedBlackTreeRoot(keys, counts, i, x - 1, redLevel-1);
            if (localRoot.left != nil)
            {
            	localRoot.left.parent = localRoot;  //Assign the parent pointer
            }
            localRoot.right = arrayToRedBlackTreeRoot(keys, counts, x + 1, j, redLevel-1);
            if (localRoot.right != nil)
            {
            	localRoot.right.parent = localRoot; //Assign the parent pointer
            }
        }
        return localRoot;   //Returns the root of the entire tree
    }
    
    /***************************************************************************/
    /* Method: handleIO
     * Input parameters: no input (VOID)
     * Return parameter: nothing to return (VOID)
     * Purpose: To handle all the interactive part of the commands entered on 
     *          the standard input. Commands are as mentioned below:
     *          increase, reduce, count, inrange, next, and previous
    /***************************************************************************/
    public void handleIO()
    {
    	String input;
		int sum, difference, countSum;
		int key1, key2, count1;                 //To be used for calling the operation functions
		Scanner scan = new Scanner(System.in);  //To access commands from the STDIN file
		
		//Keep on looking for input from STDIN till "quit" is encountered
		input = scan.next();
		while (! input.equals("quit"))          //Keep program running till "quit" is encountered
		{
			if (input.equals("increase"))
			{
				key1 = Integer.parseInt(scan.next());
				count1 = Integer.parseInt(scan.next());
				Node increase = findNode(key1, root);
				if (increase != null)                     //Node found in the tree
				{
					sum = increase.count + count1;        //Increase the count by count1
					increase.count = sum;
				}
				else                                      //Node not found in the tree
				{
					Node node = new Node(key1, count1);   //Create a new node and insert it
			        insert(node);
					sum = count1;
				}
				System.out.println(sum);      //Print the new count
				
				input = scan.next();          //Move to the next command
			}			
			else if (input.equals("reduce"))
			{
				key1 = Integer.parseInt(scan.next());
				count1 = Integer.parseInt(scan.next());
				Node reduce = findNode(key1, root);
				if (reduce != null)           //Node found
				{
					difference = reduce.count - count1;
					if (difference > 0)       //Reduce the count by count1
					{	
						reduce.count = difference;
					}
					else                      //delete the node and set difference to 0
					{
						delete(reduce);
						difference = 0;
					}
				}
				else                          //Node not found in the tree
				{
					difference = 0;
				}
				System.out.println(difference);  //Output the reduced count
				
				input = scan.next();          //Move to the next command
			}
			else if (input.equals("count"))
			{
				key1 = Integer.parseInt(scan.next());
				Node counter = findNode(key1, root);
				if (counter != null)           //Node found, print the current count value
				{
					System.out.println(counter.count);
				}
				else                          //Node not found in the tree, print 0
				{
					System.out.println(0);
				}
								
				input = scan.next();          //Move to the next command
			}
			else if (input.equals("inrange"))
			{
				key1 = Integer.parseInt(scan.next());
				key2 = Integer.parseInt(scan.next());
				countSum = inRange(key1, key2, root);   //Gives back the summed up counts value
				System.out.println(countSum);
				
				input = scan.next();                //Move to the next command
			}
			else if (input.equals("next"))
			{
				key1 = Integer.parseInt(scan.next());
				Node next = successor(key1, root);     //Returns successor
				if (next != null)
					System.out.println(next.key+" "+next.count);
				else
					System.out.println("0 0");
					
				input = scan.next();                //Move to the next command
			}
			else if (input.equals("previous"))
			{
				key1 = Integer.parseInt(scan.next());
				Node previous = predecessor(key1, root);  //Returns predecessor
				if (previous != null)
					System.out.println(previous.key+" "+previous.count);
				else
					System.out.println("0 0");
				
				input = scan.next();                //Move to the next command
			}
		}
		scan.close();
    }
    
    /***************************************************************************/
    /* Method: main
     * Input parameters: a string array to get command line arguments
     * Return parameter: nothing to return (VOID)
     * Purpose: To build initial Red Black Tree and handle all operations 
    /***************************************************************************/
	public static void main(String[] args) 
	{
		if (args.length > 0)                          //Check if filename is sent as an argument
		{
			String filename = args[0];
			bbst rbt = new bbst();	
			long stime  = System.currentTimeMillis();			
			rbt.buildInitialRBT(filename);            //To build initial Red Black Tree from file			
			rbt.handleIO();                           //To handle all operations through commands
		}
	}
}
