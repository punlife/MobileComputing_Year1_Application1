package com1032.cw1.ld00245.ld00245_todolist;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by PunLife on 02/03/2016.
 */

/**
 * Two of the lists are used to store the items and their details (subitems) respectively (listItems and listSubitems)
 * filenameList is used to store all the filenames of the to do lists that are currently saved on the device
 * preconversionfilelist is used only during gathering the list of present files before they are passed into
 * the filelist array.
 */
public class DataStorage {
    private List<String> listItems;
    private String itemFilename;
    private String finalItemString;
    private List<String> listSubitems;
    private String subitemFilename;
    private String finalSubitemString;
    private List<String> filenamelist;
    private List<File> preconversionfilelist;
    private File[] filelist;


    public DataStorage(){
        listItems = new ArrayList<>();
        finalItemString = null;
        itemFilename = null;
        listSubitems = new ArrayList<>();
        finalSubitemString = null;
        subitemFilename = null;
        filenamelist = new ArrayList<>();
        preconversionfilelist = new ArrayList<>();
    }

    public List<String> getListItems(){
        return listItems;
    }
    public void setListItems(List<String> list){
        listItems = list;
    }
    public void addToListItems(String item){
        listItems.add(item);
    }
    public List<String> getListSubitems(){
        return listSubitems;
    }
    public void setListSubitems(List<String> list){
        listSubitems = list;
    }
    public void addToListSubitems(String item){
        listSubitems.add(item);
    }
    public String getItemFilename(){
        return itemFilename;
    }
    public void setItemFilename(String itemFilename){
        this.itemFilename = itemFilename;
    }
    public String getSubitemFilename(){
        return subitemFilename;
    }
    public void setSubitemFilename(String subitemFilename){
        this.subitemFilename = "|details|"+subitemFilename;
    }
    public String getFinalItemString(){
        return finalItemString;
    }
    public void setFinalItemString(){
        //Iterate the list and string buffer
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < listItems.size(); i++) {
            sb.append(listItems.get(i) + "@");
            finalItemString = sb.toString();
        }
    }
    public String getFinalSubitemString(){
        return finalSubitemString;
    }
    public void setFinalSubitemString(){
        //Iterate the list and string buffer
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < listSubitems.size(); i++) {
            sb.append(listSubitems.get(i) + "@");
            finalSubitemString = sb.toString();
        }
    }
    public List<String> getFilenamelist(){
        return filenamelist;
    }
    public void addToFilenamelist(String item){
        filenamelist.add(item);
    }
    public void setFilenamelist(List<String> list){
        filenamelist = list;
    }
    public File[] getFilelist(){
        return filelist;
    }
    public void setFilelist(File[] list){
        filelist = list;
    }
    public void addToPreconversionfilelist(File file){
        preconversionfilelist.add(file);
    }

    /**
     * Passes all the data from preconversionfilelist into the filelist array
     */
    public void convertToArray(){
        filelist = new File[preconversionfilelist.size()];
        for (int i = 0;i<preconversionfilelist.size();i++){
            filelist[i] = preconversionfilelist.get(i);
        }
    }
    /**
     * Removes the item from the item list
     * @param positon indicates the index at which the data to be removed is
     */
    public void removeListItems(int positon){
        listItems.remove(positon);
    }
    /**
     * Removes the item from the subitem list
     * @param positon indicates the index at which the data to be removed is
     */
    public void removeListSubItems(int positon){
        listSubitems.remove(positon);
    }


}
