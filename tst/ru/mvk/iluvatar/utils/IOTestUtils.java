/**
 * (c) raptor_MVK, 2014. All rights reserved.
 */
package ru.mvk.iluvatar.utils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

public class IOTestUtils {
  @NotNull
  public static Writer getFileWriter(@NotNull String path, @NotNull String filename,
                                     @NotNull String codePage) {
    try {
      @NotNull String fullFilename = getFilename(path, filename);
      @NotNull FileOutputStream fileOutputStream = new FileOutputStream(fullFilename);
      @NotNull OutputStreamWriter outputStreamWriter =
          new OutputStreamWriter(fileOutputStream, codePage);
      return new BufferedWriter(outputStreamWriter);
    } catch (FileNotFoundException | UnsupportedEncodingException e) {
      throw new RuntimeException(e);
    }
  }

  public static void deleteFile(@NotNull String path, @NotNull String filename) {
    try {
      @NotNull Path filePath = getPath(path, filename);
      Files.delete(filePath);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @NotNull
  private static Path getPath(@NotNull String path, @NotNull String filename) {
    @NotNull FileSystem defaultFileSystem = FileSystems.getDefault();
    @Nullable Path filePath = defaultFileSystem.getPath(path, filename);
    if (filePath == null) {
      throw new RuntimeException("Path is null");
    }
    return filePath;
  }

  @NotNull
  private static String getFilename(@NotNull String path, @NotNull String filename) {
    @NotNull Path filePath = getPath(path, filename);
    @Nullable String filePathString = filePath.toString();
    if (filePathString == null) {
      throw new RuntimeException("Path string is null");
    }
    return filePathString;
  }
}
