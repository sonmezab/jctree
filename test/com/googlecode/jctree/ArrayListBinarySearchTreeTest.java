/*
 * Copyright 2014 Gaurav Saxena
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.googlecode.jctree;

import java.util.Arrays;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.googlecode.jctree.ArrayListBinarySearchTree;
import com.googlecode.jctree.NodeNotFoundException;

public class ArrayListBinarySearchTreeTest {
	@DataProvider
	public Object[][] getTree() {
		ArrayListBinarySearchTree<String> binarySearchTree = new ArrayListBinarySearchTree<String>();
		binarySearchTree.add("C6");
		binarySearchTree.add("C3");
		binarySearchTree.add("C9");
		binarySearchTree.add("C1");
		binarySearchTree.add("C4");
		binarySearchTree.add("C7");
		binarySearchTree.add("CB");
		binarySearchTree.add("C2");
		binarySearchTree.add("C5");
		binarySearchTree.add("C8");
		binarySearchTree.add("CA");
		binarySearchTree.add("CC");
		/*
								     C6
							   ______|______
							  |	            |
							  C3            C9
					    ______|__       ____|______
					   |	     |     |           |
					  C1        C4      C7          CB
			           |____     |__    |__      ___|___
			                |       |      |    |       |
			                C2      C5     C8   CA      CC
		 */
		return new Object[][]{{0, new ArrayListBinarySearchTree<String>()},{1, binarySearchTree}};
	  }

  @Test(dataProvider = "getTree")
  public void addE(int testCaseNumber, ArrayListBinarySearchTree<String> tree) throws NodeNotFoundException {
	int initialSize = tree.size();
    Assert.assertEquals(true, tree.add("C1.5"));
    Assert.assertEquals(initialSize, tree.size() - 1);
    Assert.assertEquals(true, tree.contains("C1.5"));
    switch(testCaseNumber) {
    	case 0:
    		Assert.assertEquals("C1.5", tree.root());
    		Assert.assertEquals(false, tree.add("C1.5"));
    		break;
    	case 1:
    		Assert.assertEquals("C1.5", tree.left("C2"));
    		Assert.assertEquals(true, tree.add("C0.5"));
    		Assert.assertEquals("C0.5", tree.left("C1"));
    		Assert.assertEquals(true, tree.add("CD"));
    		Assert.assertEquals("CD", tree.right("CC"));
    		Assert.assertEquals(false, tree.add("C1.5"));
    }
  }

  @Test(dataProvider = "getTree", expectedExceptions = {UnsupportedOperationException.class})
  public void addEE(int testCaseNumber, ArrayListBinarySearchTree<String> tree) throws NodeNotFoundException {
	  Assert.assertEquals(true, tree.add(tree.root(), "New"));
  }

  @Test(dataProvider = "getTree")
  public void addAllCollectionextendsE(int testCaseNumber, ArrayListBinarySearchTree<String> tree) {
	int initialSize = tree.size();
	for (String i : Arrays.asList(new String[]{"1","2","3"}))
		tree.add(i);
	Assert.assertEquals(initialSize + 3, tree.size());
	Assert.assertEquals(true, tree.contains("1"));
	Assert.assertEquals(true, tree.contains("2"));
	Assert.assertEquals(true, tree.contains("3"));
  }

  @Test(dataProvider = "getTree")
  public void addAllECollectionextendsE(int testCaseNumber, ArrayListBinarySearchTree<String> tree) throws NodeNotFoundException {
	  int initialSize = tree.size();
	  if(initialSize > 0) {
	      for (String i : Arrays.asList(new String[]{"1","2","3"}))
		    tree.add(i);
	      Assert.assertEquals(initialSize + 3, tree.size());
		  Assert.assertEquals(true, tree.contains("1"));
		  Assert.assertEquals(true, tree.contains("2"));
		  Assert.assertEquals(true, tree.contains("3"));
	  }
  }

  @Test(dataProvider = "getTree")
  public void children(int testCaseNumber, ArrayListBinarySearchTree<String> tree) throws NodeNotFoundException {
	  try {
		  tree.children(null);
		  Assert.assertEquals(false, true);
	  } catch (IllegalArgumentException e) {
		  //passed
	  }
	  try {
		  tree.children("Not present");
		  Assert.assertEquals(false, true);
	  } catch (NodeNotFoundException e) {
		  //passed
	  }
	  if(testCaseNumber == 1)
		for(String i : tree.children(tree.root()))
		  Assert.assertEquals(tree.root(), tree.parent(i));
  }

  @Test(dataProvider = "getTree")
  public void clear(int testCaseNumber, ArrayListBinarySearchTree<String> tree) {
	tree.clear();
    Assert.assertEquals(0, tree.size());
  }

  @Test(dataProvider = "getTree")
  public void commonAncestor(int testCaseNumber, ArrayListBinarySearchTree<String> tree) throws NodeNotFoundException {
	for(String i: tree)
		for(String j: tree)
			Assert.assertNotEquals(null, tree.commonAncestor(i, j));
	if(testCaseNumber == 1) {
	  Assert.assertEquals(tree.root(), tree.commonAncestor(tree.root(), tree.leaves().get(0)));
	  List<String> leaves = tree.leaves();
	  Assert.assertEquals(tree.root(), tree.commonAncestor(tree.children(tree.root()).get(0), leaves.get(leaves.size() - 1)));
	}
  }

  @Test(dataProvider = "getTree")
  public void contains(int testCaseNumber, ArrayListBinarySearchTree<String> tree) {
	  Assert.assertEquals(false, tree.contains(null));
	  Assert.assertEquals(false, tree.contains("Not present"));
	  for(String i: tree)
		  Assert.assertEquals(true, tree.contains(i));
  }

  @Test(dataProvider = "getTree")
  public void containsAll(int testCaseNumber, ArrayListBinarySearchTree<String> tree) {
	  Assert.assertEquals(false, tree.containsAll(Arrays.asList(new String[]{null})));
	  Assert.assertEquals(false, tree.containsAll(Arrays.asList(new String[]{"Not Present"})));
	  Assert.assertEquals(true, tree.containsAll(tree.inOrderTraversal())); 
  }

  @Test(dataProvider = "getTree")
  public void depth(int testCaseNumber, ArrayListBinarySearchTree<String> tree) {
	  if(testCaseNumber == 0)
		  Assert.assertEquals(0, tree.depth());
	  else if(testCaseNumber == 1) {
		  Assert.assertEquals(4, tree.depth());
		  tree.removeAll(tree.leaves());
		  Assert.assertEquals(3, tree.depth());
		  tree.removeAll(tree.leaves());
		  Assert.assertEquals(2, tree.depth());
		  tree.clear();
		  Assert.assertEquals(0, tree.depth());
	  }
  }

  
  @Test(dataProvider = "getTree")
  public void inOrderTraversal(int testCaseNumber, ArrayListBinarySearchTree<String> tree) {
	  switch(testCaseNumber) {
	  case 0:
		  Assert.assertEquals(true, tree.inOrderTraversal().isEmpty());
	  	  break;
	  case 1:
		  Assert.assertEquals(true, Arrays.equals(tree.inOrderTraversal().toArray(new String[0])
			, new String[]{"C1","C2","C3","C4","C5","C6","C7","C8","C9","CA","CB","CC"}));
		  break;
	  }
  }

  
  @Test(dataProvider = "getTree")
  public void isAncestor(int testCaseNumber, ArrayListBinarySearchTree<String> tree) throws NodeNotFoundException {
	  if(testCaseNumber == 1) {
		  Assert.assertEquals(false, tree.isAncestor(null, "C2"));
		  try {
			  tree.isAncestor("C6", null);
			  Assert.assertEquals(false, true);
		  } catch (IllegalArgumentException e) {
			  //passed;
		  }
		  try {
			  tree.isAncestor("C6", "Not present");
			  Assert.assertEquals(false, true);
		  } catch (NodeNotFoundException e) {
			  //passed;
		  }
		  Assert.assertEquals(true, tree.isAncestor("C6", "C2"));
		  Assert.assertEquals(true, tree.isAncestor("C3", "C2"));
		  Assert.assertEquals(true, tree.isAncestor("C1", "C2"));
		  Assert.assertEquals(false, tree.isAncestor("C1", "CC"));
		  Assert.assertEquals(true, tree.isAncestor("CB", "CC"));
		  Assert.assertEquals(true, tree.isAncestor("C9", "CC"));
		  Assert.assertEquals(true, tree.isAncestor("C6", "CC"));
	  }
  }
  
  @Test(dataProvider = "getTree")
  public void isDescendant(int testCaseNumber, ArrayListBinarySearchTree<String> tree) throws NodeNotFoundException {
	  if(testCaseNumber == 1) {
		  Assert.assertEquals(false, tree.isDescendant("C2", null));
		  try {
			  tree.isDescendant(null, "C6");
			  Assert.assertEquals(false, true);
		  } catch (IllegalArgumentException e) {
			  //passed;
		  }
		  try {
			  tree.isDescendant("Not present", "C6");
			  Assert.assertEquals(false, true);
		  } catch (NodeNotFoundException e) {
			  //passed;
		  }
		  Assert.assertEquals(true, tree.isAncestor("C6", "C2"));
		  Assert.assertEquals(true, tree.isAncestor("C3", "C2"));
		  Assert.assertEquals(true, tree.isAncestor("C1", "C2"));
		  Assert.assertEquals(false, tree.isAncestor("C1", "CC"));
		  Assert.assertEquals(true, tree.isAncestor("CB", "CC"));
		  Assert.assertEquals(true, tree.isAncestor("C9", "CC"));
		  Assert.assertEquals(true, tree.isAncestor("C6", "CC"));
		  
		  Assert.assertEquals(true, tree.isAncestor("C6", "C3"));
		  Assert.assertEquals(true, tree.isAncestor("C6", "C9"));
	  }
  }

  @Test(dataProvider = "getTree")
  public void isEmpty(int testCaseNumber, ArrayListBinarySearchTree<String> tree) {
	  switch(testCaseNumber) {
	  	case 0:
	  		Assert.assertEquals(true, tree.isEmpty());
	  		break;
	  	case 1:
	  		Assert.assertEquals(false, tree.isEmpty());
	  		tree.remove("C1");
	  		tree.remove("C2");
	  		Assert.assertEquals(false, tree.isEmpty());
	  		tree.clear();
	  		Assert.assertEquals(true, tree.isEmpty());
	  		break;
	  }
		  
  }

  @Test(dataProvider = "getTree")
  public void leaves(int testCaseNumber, ArrayListBinarySearchTree<String> tree) {
	  switch(testCaseNumber) {
	  	case 0:
	  		Assert.assertEquals(true, tree.leaves().isEmpty());
	  		break;
	  	case 1:
	  		/*BinarySearchTree.add("C6");
			BinarySearchTree.add("C3");
			BinarySearchTree.add("C9");
			BinarySearchTree.add("C1");
			BinarySearchTree.add("C4");
			BinarySearchTree.add("C7");
			BinarySearchTree.add("CB");
			BinarySearchTree.add("C2");
			BinarySearchTree.add("C5");
			BinarySearchTree.add("C8");
			BinarySearchTree.add("CA");
			BinarySearchTree.add("CC");*/
	  		Assert.assertEquals(tree.leaves().toArray(new String[0]), new String[]{"C2","C5","C8","CA","CC"});
	  		tree.remove("C2");
			Assert.assertEquals(true, Arrays.equals(tree.leaves().toArray(new String[0]), new String[]{"C1", "C5","C8","CA","CC"}));
	  		break;
	  }
  }
  
  @Test(dataProvider = "getTree")
  public void levelOrderTraversal(int testCaseNumber, ArrayListBinarySearchTree<String> tree) {
	  switch(testCaseNumber) {
	  	case 0:
	  		Assert.assertEquals(true, tree.levelOrderTraversal().isEmpty());
	  		break;
	  	case 1:
	  		/*BinarySearchTree.add("C6");
			BinarySearchTree.add("C6", "C1");
			BinarySearchTree.add("C6", "C2");
			BinarySearchTree.add("C1", "C1-1");
			BinarySearchTree.add("C1", "C1-2");
			BinarySearchTree.add("C2", "C2-1");
			BinarySearchTree.add("C2", "C2-2");
			BinarySearchTree.add("C1-1", "C1-1-1");
			BinarySearchTree.add("C1-1", "C1-1-2");
			BinarySearchTree.add("C1-2", "C1-2-1");
			BinarySearchTree.add("C2-1", "C2-1-1");
			BinarySearchTree.add("C2-1", "C2-1-2");*/
	  		Assert.assertEquals(tree.levelOrderTraversal().toArray(new String[0])
	  			, new String[]{"C6","C3","C9","C1","C4","C7","CB","C2","C5","C8","CA","CC"});
	  		break;
	  }
  }
  
  @Test(dataProvider = "getTree")
  public void parent(int testCaseNumber, ArrayListBinarySearchTree<String> tree) throws NodeNotFoundException {
	  switch(testCaseNumber) {
	  	case 0:
	  		 try {
	  			  tree.parent(null);
	  			  Assert.assertEquals(false, true);
	  		  } catch (IllegalArgumentException e) {
	  			  //passed
	  		  }
	  		  try {
	  			  tree.parent("Not present");
	  			  Assert.assertEquals(false, true);
	  		  } catch (NodeNotFoundException e) {
	  			  //passed
	  		  }
	  		  break;
	  	case 1:
	  		Assert.assertEquals(tree.parent("C2"), "C1");
	  		Assert.assertEquals(tree.parent("C1"), "C3");
	  		Assert.assertNull(tree.parent("C6"));
	  		break;
	  }
  }

  @Test(dataProvider = "getTree")
  public void postOrderTraversal(int testCaseNumber, ArrayListBinarySearchTree<String> tree) {
	  switch(testCaseNumber) {
	  	case 0:
	  		Assert.assertEquals(true, tree.postOrderTraversal().isEmpty());
	  		break;
	  	case 1:
	  		/*BinarySearchTree.add("C6");
			BinarySearchTree.add("C6", "C1");
			BinarySearchTree.add("C6", "C2");
			BinarySearchTree.add("C1", "C1-1");
			BinarySearchTree.add("C1", "C1-2");
			BinarySearchTree.add("C2", "C2-1");
			BinarySearchTree.add("C2", "C2-2");
			BinarySearchTree.add("C1-1", "C1-1-1");
			BinarySearchTree.add("C1-1", "C1-1-2");
			BinarySearchTree.add("C1-2", "C1-2-1");
			BinarySearchTree.add("C2-1", "C2-1-1");
			BinarySearchTree.add("C2-1", "C2-1-2");*/
	  		Assert.assertEquals(tree.postOrderTraversal().toArray(new String[0])
	  			, new String[]{"C2","C1","C5","C4","C3","C8","C7","CA","CC","CB","C9","C6"});
	  		break;
	  }
  }

  @Test(dataProvider = "getTree")
  public void preOrderTraversal(int testCaseNumber, ArrayListBinarySearchTree<String> tree) {
	  switch(testCaseNumber) {
	  	case 0:
	  		Assert.assertEquals(true, tree.preOrderTraversal().isEmpty());
	  		break;
	  	case 1:
	  		/*BinarySearchTree.add("C6");
			BinarySearchTree.add("C6", "C1");
			BinarySearchTree.add("C6", "C2");
			BinarySearchTree.add("C1", "C1-1");
			BinarySearchTree.add("C1", "C1-2");
			BinarySearchTree.add("C2", "C2-1");
			BinarySearchTree.add("C2", "C2-2");
			BinarySearchTree.add("C1-1", "C1-1-1");
			BinarySearchTree.add("C1-1", "C1-1-2");
			BinarySearchTree.add("C1-2", "C1-2-1");
			BinarySearchTree.add("C2-1", "C2-1-1");
			BinarySearchTree.add("C2-1", "C2-1-2");*/
	  		Assert.assertEquals(tree.preOrderTraversal().toArray(new String[0])
	  			, new String[]{"C6","C3","C1","C2","C4","C5","C9","C7","C8","CB","CA","CC"});
	  		break;
	  }
  }
  @SuppressWarnings("unchecked")
@Test(dataProvider = "getTree")
  public void remove(int testCaseNumber, ArrayListBinarySearchTree<String> tree) {
	  switch(testCaseNumber) {
	  	case 0:
	  		try {
	  		  tree.remove(null);
	  		  Assert.assertEquals(false, true);
	  	    } catch (IllegalArgumentException e) {
	  		  //passed
	  	    }
	  		Assert.assertEquals(false, tree.remove("Not present"));
	  		break;
	  	case 1:
	  		Assert.assertEquals(false, tree.remove("Not present"));
	  		
	  		ArrayListBinarySearchTree<String> clone = (ArrayListBinarySearchTree<String>) tree.clone();
	  		Assert.assertEquals(true, clone.remove("C2"));//delet case 1
	  		Assert.assertEquals(clone.inOrderTraversal().toArray(new String[0]), new String[]{"C1","C3","C4","C5","C6","C7","C8","C9","CA","CB","CC"});
	  		Assert.assertEquals(clone.preOrderTraversal().toArray(new String[0]), new String[]{"C6","C3","C1","C4","C5","C9","C7","C8","CB","CA","CC"});
	  		
	  		clone = (ArrayListBinarySearchTree<String>) tree.clone();
	  		Assert.assertEquals(true, clone.remove("C1"));//delete case 2
	  		Assert.assertEquals(clone.inOrderTraversal().toArray(new String[0]), new String[]{"C2","C3","C4","C5","C6","C7","C8","C9","CA","CB","CC"});
	  		Assert.assertEquals(clone.preOrderTraversal().toArray(new String[0]), new String[]{"C6","C3","C2","C4","C5","C9","C7","C8","CB","CA","CC"});
	  		
	  		clone = (ArrayListBinarySearchTree<String>) tree.clone();
	  		Assert.assertEquals(true, clone.remove("C6"));//delete case 3
	  		Assert.assertEquals(clone.inOrderTraversal().toArray(new String[0]), new String[]{"C1","C2","C3","C4","C5","C7","C8","C9","CA","CB","CC"});
	  		Assert.assertEquals(true, clone.remove("C1"));//delete twice and check
	  		Assert.assertEquals(clone.inOrderTraversal().toArray(new String[0]), new String[]{"C2","C3","C4","C5","C7","C8","C9","CA","CB","CC"});
	  		break;
	  }
  }
  @Test(dataProvider = "getTree")
  public void removeAll(int testCaseNumber, ArrayListBinarySearchTree<String> tree) {
	  switch(testCaseNumber) {
	  	case 0:
	  		Assert.assertEquals(true, tree.preOrderTraversal().isEmpty());
	  		break;
	  	case 1:
	  		tree.removeAll(Arrays.asList(new String[]{}));
	  		Assert.assertEquals(true, Arrays.equals(tree.inOrderTraversal().toArray(new String[0])
	  				, new String[]{"C1","C2","C3","C4","C5","C6","C7","C8","C9","CA","CB","CC"}));
	  		tree.removeAll(Arrays.asList(new String[]{"C6","C1","C2"}));
	  		Assert.assertEquals(tree.inOrderTraversal().toArray(new String[0]), new String[]{"C3","C4","C5","C7","C8","C9","CA","CB","CC"});
	  		break;
	  }
  }

  @Test(dataProvider = "getTree")
  public void retainAll(int testCaseNumber, ArrayListBinarySearchTree<String> tree) {
   try {
	   tree.retainAll(Arrays.asList(new String[]{""}));
   } catch (UnsupportedOperationException e) {
	   //passed
   }
  }

  @Test(dataProvider = "getTree")
  public void root(int testCaseNumber, ArrayListBinarySearchTree<String> tree) {
	  switch(testCaseNumber) {
	  	case 0:
	  		Assert.assertEquals(null, tree.root());
	  		break;
	  	case 1:
	  		Assert.assertEquals("C6", tree.root());
	  		break;
	  }
  }

  @Test(dataProvider = "getTree")
  public void siblings(int testCaseNumber, ArrayListBinarySearchTree<String> tree) throws NodeNotFoundException {
	  switch(testCaseNumber) {
	  	case 0:
	  		try {
	  		  tree.siblings(null);
	  		  Assert.assertEquals(false, true);
	  	    } catch (IllegalArgumentException e) {
	  		  //passed
	  	    }
	  	    try {
	  		  tree.parent("Not present");
	  		  Assert.assertEquals(false, true);
	  	    } catch (NodeNotFoundException e) {
	  		  //passed
	  	    }
	  		break;
	  	case 1:
	  		/*BinarySearchTree.add("C6");
			BinarySearchTree.add("C6", "C1");
			BinarySearchTree.add("C6", "C2");
			BinarySearchTree.add("C1", "C1-1");
			BinarySearchTree.add("C1", "C1-2");
			BinarySearchTree.add("C2", "C2-1");
			BinarySearchTree.add("C2", "C2-2");
			BinarySearchTree.add("C1-1", "C1-1-1");
			BinarySearchTree.add("C1-1", "C1-1-2");
			BinarySearchTree.add("C1-2", "C1-2-1");
			BinarySearchTree.add("C2-1", "C2-1-1");
			BinarySearchTree.add("C2-1", "C2-1-2");*/
	  		Assert.assertEquals(tree.siblings("C6").toArray(new String[0]), new String[]{});
	  		Assert.assertEquals(tree.siblings("C3").toArray(new String[0]), new String[]{"C9"});
	  		Assert.assertEquals(tree.siblings("C9").toArray(new String[0]), new String[]{"C3"});
	  		break;
	  }
  }

  @Test(dataProvider = "getTree")
  public void size(int testCaseNumber, ArrayListBinarySearchTree<String> tree) {
	  switch(testCaseNumber) {
	  	case 0:
	  		Assert.assertEquals(0, tree.size());
	  		break;
	  	case 1:
	  		Assert.assertEquals(12, tree.size());
	  		tree.remove("C2");
	  		Assert.assertEquals(11, tree.size());
	  		tree.remove("C6");
	  		Assert.assertEquals(10, tree.size());
	  		break;
	  }
  }

  @Test(dataProvider = "getTree")
  public void toArray(int testCaseNumber, ArrayListBinarySearchTree<String> tree) {
	  switch(testCaseNumber) {
	  	case 0:
	  		Assert.assertEquals(new String[]{}, tree.toArray());
	  		break;
	  	case 1:
	  		Assert.assertEquals(tree.toArray(), new String[]{"C1","C2","C3","C4","C5","C6","C7","C8","C9","CA","CB","CC"});
	  		break;
	  }
  }

  @Test(dataProvider = "getTree")
  public void toArrayT(int testCaseNumber, ArrayListBinarySearchTree<String> tree) {
	  switch(testCaseNumber) {
	  	case 0:
	  		Assert.assertEquals(new String[]{}, tree.toArray(new String[0]));
	  		break;
	  	case 1:
	  		Assert.assertEquals(tree.toArray(new String[0])
	  				, new String[]{"C1","C2","C3","C4","C5","C6","C7","C8","C9","CA","CB","CC"});
	  		break;
	  }
  }
  @Test(dataProvider = "getTree")
  public void equals(int testCaseNumber, ArrayListBinarySearchTree<String> tree) throws NodeNotFoundException {
	  switch(testCaseNumber) {
	  	case 0:
	  		ArrayListBinarySearchTree<String> BinarySearchTree = new ArrayListBinarySearchTree<String>();
	  		Assert.assertEquals(true, tree.equals(BinarySearchTree));
	  		BinarySearchTree.add("Root2");
	  		Assert.assertEquals(false, tree.equals(BinarySearchTree));
	  		break;
	  	case 1:
	  		@SuppressWarnings("unchecked")
			ArrayListBinarySearchTree<String> clone = (ArrayListBinarySearchTree<String>) tree.clone();
	  		@SuppressWarnings("unchecked")
			ArrayListBinarySearchTree<String> clone2 = (ArrayListBinarySearchTree<String>) tree.clone();
	  		Assert.assertEquals(true, tree.equals(clone));
	  		clone.remove("C2");
	  		Assert.assertEquals(false, tree.equals(clone));
	  		clone2.add("CD");
	  		Assert.assertEquals(false, tree.equals(clone2));
	  		break;
	  }
  }
  @Test(dataProvider = "getTree")
  public void left(int testCaseNumber, ArrayListBinarySearchTree<String> tree) throws NodeNotFoundException {
	  switch(testCaseNumber) {
	  	case 1:
	  		try {
	  			tree.left(null);
	  			Assert.assertEquals(false, true);
	  		} catch(IllegalArgumentException e) {
	  			//passed
	  		}
	  		try {
	  			tree.left("not present");
	  			Assert.assertEquals(false, true);
	  		} catch(NodeNotFoundException e) {
	  			//passed
	  		}
	  		break;
	  	case 2:
	  		Assert.assertEquals(tree.left("C6"), "C3");
	  		Assert.assertEquals(tree.left("C3"), "C1");
	  		Assert.assertNull(tree.left("C1"));
	  		Assert.assertNull(tree.left("C2"));
	  		break;
	  }
  }
  @Test(dataProvider = "getTree")
  public void right(int testCaseNumber, ArrayListBinarySearchTree<String> tree) throws NodeNotFoundException {
	  switch(testCaseNumber) {
	  	case 1:
	  		try {
	  			tree.right(null);
	  			Assert.assertEquals(false, true);
	  		} catch(IllegalArgumentException e) {
	  			//passed
	  		}
	  		try {
	  			tree.right("not present");
	  			Assert.assertEquals(false, true);
	  		} catch(NodeNotFoundException e) {
	  			//passed
	  		}
	  		break;
	  	case 2:
	  		Assert.assertEquals(tree.right("C6"), "C9");
	  		Assert.assertEquals(tree.right("C3"), "C4");
	  		Assert.assertNull(tree.right("C7"));
	  		Assert.assertNull(tree.right("C2"));
	  		break;
	  }
  }
  @Test(dataProvider = "getTree")
  public void successor(int testCaseNumber, ArrayListBinarySearchTree<String> tree) throws NodeNotFoundException {
	  switch(testCaseNumber) {
	  	case 1:
	  		try {
	  			tree.predecessor(null);
	  			Assert.assertEquals(false, true);
	  		} catch(IllegalArgumentException e) {
	  			//passed
	  		}
	  		try {
	  			tree.predecessor("not present");
	  			Assert.assertEquals(false, true);
	  		} catch(NodeNotFoundException e) {
	  			//passed
	  		}
	  		break;
	  	case 2:
	  		Assert.assertEquals(tree.successor("C5"), "C6");
	  		Assert.assertEquals(tree.successor("C2"), "C3");
	  		Assert.assertEquals(tree.successor("C4"), "C5");
	  		Assert.assertEquals(tree.successor("C9"), "CA");
	  		break;
	  }
  }
  @Test(dataProvider = "getTree")
  public void predecessor(int testCaseNumber, ArrayListBinarySearchTree<String> tree) throws NodeNotFoundException {
	  switch(testCaseNumber) {
	  	case 1:
	  		try {
	  			tree.predecessor(null);
	  			Assert.assertEquals(false, true);
	  		} catch(IllegalArgumentException e) {
	  			//passed
	  		}
	  		try {
	  			tree.predecessor("not present");
	  			Assert.assertEquals(false, true);
	  		} catch(NodeNotFoundException e) {
	  			//passed
	  		}
	  		break;
	  	case 2:
	  		Assert.assertEquals(tree.predecessor("C6"), "C5");
	  		Assert.assertEquals(tree.predecessor("C3"), "C2");
	  		Assert.assertEquals(tree.predecessor("C5"), "C4");
	  		Assert.assertEquals(tree.predecessor("CA"), "C9");
	  		break;
	  }
  }
}