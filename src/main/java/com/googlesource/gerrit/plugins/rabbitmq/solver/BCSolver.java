// Copyright (C) 2015 The Android Open Source Project
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
package com.googlesource.gerrit.plugins.rabbitmq.solver;

import static com.googlesource.gerrit.plugins.rabbitmq.PropertiesStore.FILE_EXT;
import static com.googlesource.gerrit.plugins.rabbitmq.PropertiesStore.SITE_DIR;

import com.google.gerrit.extensions.annotations.PluginData;
import com.google.gerrit.extensions.annotations.PluginName;
import com.google.gerrit.server.config.SitePaths;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystemException;
import java.nio.file.Files;
import java.nio.file.Path;

public class BCSolver implements Solver {

  private final static String DEFAULT_SITE_NAME = "default";
  private static final Logger LOGGER = LoggerFactory.getLogger(BCSolver.class);

  private final String pluginName;
  private final Path pluginDataDir;
  private final Path etcDir;

  @Inject
  public BCSolver(
      @PluginName final String pluginName,
      @PluginData final File pluginData,
      final SitePaths sites
      ) {
    this.pluginName = pluginName;
    this.pluginDataDir = pluginData.toPath();
    this.etcDir = sites.etc_dir.toPath();
  }

  /**
   * old : etc/rabbitmq.config
   *
   * new : data/rabbitmq/rabbitmq.config
   *       data/rabbitmq/site/default.config
   */
  public void solve() {
    try {
      Path oldFile = etcDir.resolve(pluginName + FILE_EXT);
      Path newFile = pluginDataDir.resolve(pluginName + FILE_EXT);
      Path siteDir = pluginDataDir.resolve(SITE_DIR);

      Files.createDirectories(siteDir);
      Files.move(oldFile, newFile);
      Files.createFile(siteDir.resolve(DEFAULT_SITE_NAME + FILE_EXT));
    } catch (Exception ex) {
    }
  }
}