# FRAGEMENT TRANSACTION #


**QUESTION**

Imagine you have 3 Fragments

(1) (2) (3)

I want the user to be able to navigate (1) > (2) > (3) but on the way back (pressing back button) (3) > (1).



[[Question Link](http://stackoverflow.com/questions/12529499/problems-with-android-fragment-back-stack)]



**EXPLANATION** (on what's going on here?):

If we keep in mind that .replace() = .remove().add() (that we know by documentation )

> Replace an existing fragment that was added to a container. This is
> essentially the same as calling remove(Fragment) for all currently
> added fragments that were added with the same containerViewId and then
> add(int, Fragment, String) with the same arguments given here.

then what's happening is like this (I'm adding numbers to the frag to make it more clear):

    // transaction.replace(R.id.detailFragment, frag1);
    Transaction.remove(null).add(frag1)  // frag1 on view
    
    // transaction.replace(R.id.detailFragment, frag2).addToBackStack(null);
    Transaction.remove(frag1).add(frag2).addToBackStack(null)  // frag2 on view
    
    // transaction.replace(R.id.detailFragment, frag3);
    Transaction.remove(frag2).add(frag3)  // frag3 on view

(here all misleading stuff starts to happen)

Remember that .addToBackStack() is saving only TRANSACTION not the FRAGMENT as itself!

So now we have frag3 on the layout:

    < press back button >
    // System pops the back stack and find the following saved back entry to be reversed:
    // [Transaction.remove(frag1).add(frag2)]
    // so the system makes that transaction backward!!!
    // tries to remove frag2 (is not there, so it ignores) and re-add(frag1)
    // make notice that system doesn't realise that there's a frag3 and does nothing with it
    // so it still there attached to view
    Transaction.remove(null).add(frag1) //frag1, frag3 on view (OVERLAPPING)
    
    // transaction.replace(R.id.detailFragment, frag2).addToBackStack(null);
    Transaction.remove(frag3).add(frag2).addToBackStack(null)  //frag2 on view
    
    < press back button >
    // system makes saved transaction backward
    Transaction.remove(frag2).add(frag3) //frag3 on view
    
    < press back button >
    // no more entries in BackStack
    < app exits >

**Possible solution:**

consider implementing [FragmentManager.BackStackChangedListener][1] to watch for changes in the back stack and apply your logic in [onBackStackChanged()][2] methode:

 - trace a count of transaction;
 - check particular transaction by name (FragmentTransaction [addToBackStack(String name)][3]);
 - etc.


  [1]: http://developer.android.com/reference/android/app/FragmentManager.OnBackStackChangedListener.html
  [2]: http://developer.android.com/reference/android/app/FragmentManager.OnBackStackChangedListener.html#onBackStackChanged%28%29
  [3]: http://developer.android.com/reference/android/app/FragmentTransaction.html#addToBackStack%28java.lang.String%29