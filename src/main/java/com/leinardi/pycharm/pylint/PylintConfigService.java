/*
 * Copyright 2021 Roberto Leinardi.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.leinardi.pycharm.pylint;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.project.Project;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@State(name = "PylintConfigService", storages = {@Storage("pylint.xml")})
public class PylintConfigService implements PersistentStateComponent<PylintConfigService> {
    public PylintConfigService() {
        customPylintPath = "";
        pylintArguments = "";
        pylintrcPath = "";
        scanBeforeCheckin = true;
    }

    private String customPylintPath;
    private String pylintrcPath;
    private String pylintArguments;
    private boolean scanBeforeCheckin;

    public String getCustomPylintPath() {
        return customPylintPath;
    }

    public void setCustomPylintPath(String pathToPylint) {
        this.customPylintPath = pathToPylint;
    }

    public String getPylintrcPath() {
        return pylintrcPath;
    }

    public void setPylintrcPath(String pathToPylintrcFile) {
        this.pylintrcPath = pathToPylintrcFile;
    }

    public String getPylintArguments() {
        return pylintArguments;
    }

    public void setPylintArguments(String pylintArguments) {
        this.pylintArguments = pylintArguments;
    }

    public boolean isScanBeforeCheckin() {
        return scanBeforeCheckin;
    }

    public void setScanBeforeCheckin(boolean scanBeforeCheckin) {
        this.scanBeforeCheckin = scanBeforeCheckin;
    }

    @Nullable
    @Override
    public PylintConfigService getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull PylintConfigService config) {
        XmlSerializerUtil.copyBean(config, this);
    }

    @Nullable
    public static PylintConfigService getInstance(Project project) {
        return ServiceManager.getService(project, PylintConfigService.class);
    }
}
