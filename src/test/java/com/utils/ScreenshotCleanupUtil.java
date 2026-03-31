package com.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.nio.file.FileVisitResult;

public class ScreenshotCleanupUtil {
    private static final Logger logger = LoggerFactory.getLogger(ScreenshotCleanupUtil.class);
    
    /**
     * Delete screenshots older than specified days
     * 
     * @param screenshotDirectory Path to screenshot directory
     * @param daysToKeep Number of days to keep screenshots (default: 7)
     * @return Number of files deleted
     */
    public static int deleteOldScreenshots(String screenshotDirectory, int daysToKeep) {
        File directory = new File(screenshotDirectory);
        
        if (!directory.exists() || !directory.isDirectory()) {
            logger.warn("Screenshot directory does not exist: {}", screenshotDirectory);
            return 0;
        }
        
        Instant cutoffTime = Instant.now().minus(daysToKeep, ChronoUnit.DAYS);
        List<File> deletedFiles = new ArrayList<>();
        
        try {
            Files.walkFileTree(Paths.get(screenshotDirectory), new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    // Only delete image files
                    String fileName = file.getFileName().toString().toLowerCase();
                    if (fileName.endsWith(".png") || fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
                        Instant fileTime = attrs.creationTime().toInstant();
                        
                        if (fileTime.isBefore(cutoffTime)) {
                            try {
                                Files.delete(file);
                                deletedFiles.add(file.toFile());
                                logger.debug("Deleted old screenshot: {}", file.getFileName());
                            } catch (IOException e) {
                                logger.error("Failed to delete file: {}", file.getFileName(), e);
                            }
                        }
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
            
            if (!deletedFiles.isEmpty()) {
                logger.info("Cleaned up {} old screenshots (older than {} days)", deletedFiles.size(), daysToKeep);
            } else {
                logger.info("No old screenshots found to clean up");
            }
            
            return deletedFiles.size();
            
        } catch (IOException e) {
            logger.error("Error cleaning up screenshots from {}", screenshotDirectory, e);
            return 0;
        }
    }
    
    /**
     * Delete screenshots older than 7 days (default)
     */
    public static int deleteOldScreenshots(String screenshotDirectory) {
        return deleteOldScreenshots(screenshotDirectory, 7);
    }
    
    /**
     * Delete all screenshots in the directory
     * 
     * @param screenshotDirectory Path to screenshot directory
     * @return Number of files deleted
     */
    public static int deleteAllScreenshots(String screenshotDirectory) {
        File directory = new File(screenshotDirectory);
        
        if (!directory.exists() || !directory.isDirectory()) {
            logger.warn("Screenshot directory does not exist: {}", screenshotDirectory);
            return 0;
        }
        
        int deletedCount = 0;
        File[] files = directory.listFiles();
        
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    String fileName = file.getName().toLowerCase();
                    if (fileName.endsWith(".png") || fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
                        if (file.delete()) {
                            deletedCount++;
                            logger.debug("Deleted screenshot: {}", file.getName());
                        }
                    }
                }
            }
            logger.info("Deleted {} screenshots from {}", deletedCount, screenshotDirectory);
        }
        
        return deletedCount;
    }
    
    /**
     * Get the total size of screenshots in MB
     */
    public static double getScreenshotDirectorySize(String screenshotDirectory) {
        File directory = new File(screenshotDirectory);
        
        if (!directory.exists() || !directory.isDirectory()) {
            return 0;
        }
        
        long totalSize = 0;
        File[] files = directory.listFiles();
        
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    String fileName = file.getName().toLowerCase();
                    if (fileName.endsWith(".png") || fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
                        totalSize += file.length();
                    }
                }
            }
        }
        
        return totalSize / (1024.0 * 1024.0); // Convert to MB
    }
    
    /**
     * Count screenshot files in directory
     */
    public static int countScreenshots(String screenshotDirectory) {
        File directory = new File(screenshotDirectory);
        
        if (!directory.exists() || !directory.isDirectory()) {
            return 0;
        }
        
        int count = 0;
        File[] files = directory.listFiles();
        
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    String fileName = file.getName().toLowerCase();
                    if (fileName.endsWith(".png") || fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
                        count++;
                    }
                }
            }
        }
        
        return count;
    }
}
