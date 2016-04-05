package com.zzc.androidaidl;

import com.zzc.androidaidl.Book;
import com.zzc.androidaidl.INewBookArrivedListener;

//服务器端功能接口
interface IBookManager {
    List<Book> getBookList();

    void addBook(in Book book);

    void registerListener(INewBookArrivedListener listener);

    void unregisterListener(INewBookArrivedListener listener);
}