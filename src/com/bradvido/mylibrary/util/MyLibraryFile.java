/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.bradvido.mylibrary.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.bradvido.mylibrary.util.Constants.*;
import static com.bradvido.util.tools.BTVTools.*;

public class MyLibraryFile extends com.bradvido.xbmc.util.XBMCFile
{
    int episodeNumber=-1, seasonNumber=-1, year=-1;
    String tvdbId;
    String originalAirDate;
    String finalLocation = null;
    boolean hasBeenLookedUpOnTVDB = false;
    private boolean skippedBecauseAlreadyArchived = false;
    
    protected Subfolder subfolder;
    
    ///for multi-file vidoes
    private boolean isDuplicate = false;
    private boolean isMultiFile = false;
    public List<MyLibraryFile> duplicateVideos = null;
    private MyLibraryFile originalVideo = null;
        
    
    	//AngryCamel - 20120805 2351 - Added runtime
    public MyLibraryFile(String fileOrDir, String fanart, String file, String fileLabel, String thumbnail, int runtime, String parentPath, Subfolder matchingSubfolder)
    {
        super(fileOrDir, fanart, file, fileLabel, thumbnail, runtime, parentPath);        
        this.subfolder = matchingSubfolder;
    }
    
    //AngryCamel - 20120805 2351 - Added runtime
    public MyLibraryFile(String fileOrDir, String fanart, String file, String fileLabel, String thumbnail, int runtime)
    {
        super(fileOrDir, fanart, file, fileLabel, thumbnail, runtime);
        
    }
    
    //limited constructor used in manual archiving
    public MyLibraryFile(String fullPlayonPath)
    {
        super(fullPlayonPath);        
    }
    
    public static void copyVideoMetaData(MyLibraryFile source, MyLibraryFile dest)
    {
        dest.setTitle(source.getTitle());
        dest.setType(source.getType());
        dest.setArtist(source.getArtist());
        dest.setEpisodeNumber(source.getEpisodeNumber());
        dest.setFileLabel(source.getFileLabel());
        dest.setFinalLocation(source.getFinalLocation());
        dest.setHasBeenLookedUpOnTVDB(source.hasBeenLookedUpOnTVDB());
        dest.setMultiFileVideo(source.isMultiFileVideo());
        dest.setOriginalAirDate(source.getOriginalAirDate());
        if(source.isDuplicate()) dest.setAsDuplicateTo(source.getOriginalVideo());
        else dest.setOriginalVideo(source.getOriginalVideo());
        dest.setSeasonNumber(source.getSeasonNumber());
        dest.setSeries(source.getSeries());
        dest.setSkippedBecauseAlreadyArchived(source.skippedBecauseAlreadyArchived);
        dest.setSubfolder(source.getSubfolder());
        dest.setTVDBId(source.getTVDBId());
        dest.setYear(source.getYear());
    }
    
       public void setSubfolder(Subfolder subf)
    {
        this.subfolder = subf;
    }
    
    public Subfolder getSubfolder()
    {
        return this.subfolder;
    }
    
    public void setOriginalVideo(MyLibraryFile video)
    {
        this.originalVideo = video;
    }
    public MyLibraryFile getOriginalVideo()
    {
        return originalVideo;
    }
    public void setSkippedBecauseAlreadyArchived(boolean b)
    {
        this.skippedBecauseAlreadyArchived = b;
    }
    public boolean skippedBecauseAlreadyArchived()
    {
        return skippedBecauseAlreadyArchived;
    }
    public boolean isMultiFileVideo()
    {
        return isMultiFile;
    }
    public boolean isDuplicate()
    {
        return isDuplicate;
    }
    public void setMultiFileVideo(boolean b)
    {
        this.isMultiFile = b;
        if(duplicateVideos == null)
            duplicateVideos = new ArrayList<MyLibraryFile>();
    }
    public void setAsDuplicateTo(MyLibraryFile originalVideo)
    {
        this.isDuplicate=true;
        this.originalVideo = originalVideo;

    }
    public void addDuplicateVideo(MyLibraryFile video)
    {
        duplicateVideos.add(video);
    }
    public void setHasBeenLookedUpOnTVDB(boolean hasBeenLookedUpOnTVDB)
    {
        this.hasBeenLookedUpOnTVDB = hasBeenLookedUpOnTVDB;
    }
    public boolean hasBeenLookedUpOnTVDB()
    {
        return hasBeenLookedUpOnTVDB;
    }
    public void setFinalLocation(String finalLocation)
    {
        this.finalLocation = finalLocation;
    }
    public String getFinalLocation()
    {
        return finalLocation;
    }
      public boolean addTVDBSeriesEpisodeNumbers(String seasonNumber, String episodeNumber)
    {
        try
        {
            int s = Integer.parseInt(seasonNumber);
            int e = Integer.parseInt(episodeNumber);
            setSeasonNumber(s);
            setEpisodeNumber(e);
            return true;
        }
        catch(Exception x)
        {
            return false;//tvdb numbers are not integers
        }
    }
    public String getOriginalAirDate()
    {
        return originalAirDate;
    }
    public void setOriginalAirDate(String originalAirDate)
    {
        this.originalAirDate = originalAirDate;
    }
    public boolean isTVDBIdOverridden()
    {
        return valid(getTVDBId());
    }
    public String getTVDBId()
    {
        return tvdbId;
    }
    public void setTVDBId(String id)
    {
        this.tvdbId = id;
    }
    public boolean hasValidMetaData()
    {        
        if(isMovie() && valid(getTitle())) return true;
        if(isTvShow() && valid(getSeries()) && getSeasonNumber() > -1 && getEpisodeNumber() > -1) return true;//title not required
        if(isMusicVideo() && valid(getTitle()) && valid(getArtist())) return true;

    	//AngryCamel - 20120817 1620 - Added generic
        if(isGeneric() && valid(getSeries()) && valid(getTitle())) return true;
        
        return false;
    }
    public void setArtist(String artist)
    {
        this.artist = artist;
    }
    public String getArtist()
    {
        return artist;
    }
    public String getSeasonEpisodeNaming()
    {
        return "S"+getPaddedSeasonNumber() + "E"+getPaddedEpisodeNumber();
    }
     public String getPaddedEpisodeNumber()
    {
        return padNumber(getEpisodeNumber());
    }
    public String getPaddedSeasonNumber()
    {
        return padNumber(getSeasonNumber());
    }
    //helper functions
    private String padNumber(int num)
    {
        if(num < 10) return "0"+num;
        else return ""+num;
    }

    public int getYear()
    {
        return year;
    }
    public void setYear(int year)
    {
        this.year = year;
    }
    public boolean hasYear()
    {
        return year != -1;
    }
    
    
    public void setSeries(String series)
    {                
    	//AngryCamel - 20120817 1620
    	//   Check if there is a forced series in the subfolder config and apply it instead of whatever was passed.
    	try {
                if(valid(this.getSubfolder().getForceSeries()))
                {
                        this.series = stripExtras(this.getSubfolder().getForceSeries());
                }
                else
                {
                        this.series = stripExtras(series);
                }
        } catch (Exception e) {
                this.series = stripExtras(series);
        }
    }
    
    public String getSeries()
    {
        return series;
    }
    public void setTitle(String title)
    {
        this.title = stripExtras(title);
    }
    public String getTitle()
    {
        return title;
    }
    public void setSeasonNumber(int seasonNumber)
    {
        this.seasonNumber = seasonNumber;
    }
    public void setEpisodeNumber(int episodeNumber)
    {
        this.episodeNumber = episodeNumber;
    }
	
	//AngryCamel - 20120805 2351
    public void setRuntime(int runtime)
    {
        this.runtime = runtime;
    }
	
    public int getSeasonNumber()
    {
        return seasonNumber;
    }
    public int getEpisodeNumber()
    {
        return episodeNumber;
    }
    public void setType(String type)
    {
        this.type = type;
    }
    public String getType()
    {
        return type;
    }
    public boolean knownType()
    {
    	//AngryCamel - 20120817 1620 - added isGeneric
        return isMovie() || isTvShow() || isMusicVideo() || isGeneric();
    }
    public boolean isTvShow()
    {
        return TV_SHOW.equals(type);
    }
    public boolean isMovie()
    {
        return MOVIE.equals(type);
    }
    public boolean isMusicVideo()
    {
        return MUSIC_VIDEO.equals(type);
    }
    
	//AngryCamel - 20120817 1620 - added isGeneric
    public boolean isGeneric()
    {
        return GENERIC.equals(type);
    }
    
    public String getFileList()
    {
        if(!isMultiFileVideo()) return file;
        else
        {
            String files = "";
            //get a mapof all the file labels and the corresponding file
            Map<String, String> fileMap = new TreeMap<String,String>();

            for(Iterator<MyLibraryFile> it = duplicateVideos.iterator(); it.hasNext();)
            {
                MyLibraryFile video = it.next();
                if(video.getFileLabel().toLowerCase().contains("full"))
                {
                    return video.getFile();//this is a full file, dont need to get the others
                }
                fileMap.put(video.getFileLabel(), video.getFile());//get only unique file labels
            }
            //no full files exist, return a list of the parts, in order
            //the list is already alphabetical, so assume the files are in the correct order, jsut pick a source
            String sourceId = null;
            boolean useAllFiles = false;
            for(Map.Entry<String,String> entry : fileMap.entrySet())
            {
                //use the first source in the list
                String label = entry.getKey();
                String file = entry.getValue();
                 if(sourceId == null && !useAllFiles)
                 {
                     //look for the identifier, that is everything up to the part x of the label
                     if(label.toLowerCase().contains("part"))
                        sourceId = label.substring(0, label.toLowerCase().indexOf("part"));
                     else useAllFiles = true;//since we can't find the part identifier, just use all files
                 }

                if(useAllFiles || label.startsWith(sourceId))
                {                    
                    files += file + LINE_BRK;
                }
            }
            return files.replaceAll("\\r\\n"+"$", "");//remove the last line break ($ means end of string)
        }
    }
    
    public String stripExtras(String source)
    {
        if(!valid(source)) return source;        
        source = source.trim();
        
        String yearDigits = "[1,2][0-9]{3}";//match 1000's and 2000's
        String optionalYear =" (\\("+yearDigits+"\\)|\\["+yearDigits+"\\])";//matches " (nnnn)" or " [nnnn]"
        Pattern yearPattern = Pattern.compile(optionalYear, Pattern.CASE_INSENSITIVE);
        Matcher m = yearPattern.matcher(source);
        while(m.find())
        {
            String match = m.group().trim();
            //make sure it is at the end of the string
            if(match.equals(source.substring(source.length()-match.length(), source.length())))
            {
                source = source.substring(0, source.length()-match.length()).trim();
                setYear(Integer.parseInt(match.substring(1,match.length()-1)));//strip the () or [] from the year and parse it
                break;
            }
        }
        
        source = tools.stripExtraLabels(source);//remove (hd) type labeling
        
        return source;
    }
    
    public boolean isWithinLimits(Subfolder subf){
        boolean withinLimits = true;//default for non-tv types
        if(isTvShow())
        {
            //check series limit
            withinLimits = subf.canAddAnotherSeries(getSeries());
        }        
        return withinLimits;
    }
    
}
