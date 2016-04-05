// INewBookArrivedListener.aidl
package com.zzc.androidaidl;

import com.zzc.androidaidl.Book;
// Declare any non-default types here with import statements

interface INewBookArrivedListener {


            void onNewBookArrived(in Book newBook);
}
