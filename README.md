# Extract My Android App data

Extract android data files from android data folder.
It extracts all the files inside data folder of android application within package folder.
You dont need android device to be rooted but it only works for debuggable application and not with release builds.
Tested with Sony, Nexus and sometimes face problem with Samsung Manufactured android devices.

## How it works:
1. It asks you to enter package name.
2. It will identify all files in the android package name folder and create the directory in device's sdcard.
3. Files will be extracted into the android device sdcard. For Example: "/sdcard/com.example.myapp"
4. It will create a directory where .bat/.sh file is executed with same name as package entered and will pull all files into that respective directory.

## Update:
If you are having a rooted device then you can access any package data.
Code has been updated, need to update the JAR in the download link.

#### Note
Everytime files are extracted it will be stored in sdcard and older files will be renewed but on Desktop/Finder new directory will be created.

Download executable file from [Android Extract Data](https://goo.gl/eqTVfm)

### License
```
   Copyright (C) 2016 Mohammed Rampurawala

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
```
