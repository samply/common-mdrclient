# Samply.Common.MDRClient

Samply.Common.MDRClient is a Java library which provides convenient methods
to access metadata from the Samply.MDR. Those  methods provide access to
the metadata of one specific data element or of multiple data elements meeting
some specified criteria.

# Features

- retrieve metadata from Samply.MDR via REST
- access to data elements by URN, data element group or namespace
- access all data elements or only those bound to a specific user (access token
  from Samply.Auth)
- search for data elements

# Build

In order to build this project, you need to configure maven properly.  See
[Samply.Maven](https://bitbucket.org/medinfo_mainz/samply.maven) for more
information.

Use maven to build the jar:

``` 
mvn clean package
```

Use it as a dependency:

```xml
<dependency>
    <groupId>de.samply</groupId>
    <artifactId>mdrclient</artifactId>
    <version>${version}</version>
</dependency>
```

 ## License
        
 Copyright 2020 The Samply Development Community
        
 Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at
        
 http://www.apache.org/licenses/LICENSE-2.0
        
 Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 
