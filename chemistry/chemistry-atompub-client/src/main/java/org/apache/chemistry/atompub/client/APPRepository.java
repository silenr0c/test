/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Authors:
 *     Bogdan Stefanescu, Nuxeo
 */
package org.apache.chemistry.atompub.client;

import java.io.InputStream;
import java.io.Serializable;
import java.net.URI;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.apache.chemistry.Connection;
import org.apache.chemistry.Repository;
import org.apache.chemistry.RepositoryInfo;
import org.apache.chemistry.SPI;
import org.apache.chemistry.Type;
import org.apache.chemistry.atompub.CMIS;
import org.apache.chemistry.atompub.client.connector.APPContentManager;
import org.apache.chemistry.atompub.client.connector.Request;
import org.apache.chemistry.atompub.client.connector.Response;
import org.apache.chemistry.atompub.client.stax.ReadContext;

/**
 * An APP client repository proxy
 */
public class APPRepository implements Repository {

    protected APPContentManager cm;

    protected RepositoryInfo info;

    protected String id;

    protected Map<String, Type> typeRegistry;

    protected Map<String, String> collections = new HashMap<String, String>();

    protected Map<Class<?>, Object> singletons = new Hashtable<Class<?>, Object>();

    public APPRepository(APPContentManager cm) {
        this(cm, null);
    }

    public APPRepository(APPContentManager cm, RepositoryInfo info) {
        this.cm = cm;
        this.info = info;
    }

    public void setInfo(RepositoryInfo info) {
        this.info = info;
    }

    public ContentManager getContentManager() {
        return cm;
    }

    public String getId() {
        if (id == null) {
            id = info.getId();
        }
        return id;
    }

    public String getName() {
        return info.getName();
    }

    public URI getURI() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public SPI getSPI() {
        loadTypes();
        return new APPConnection(this);
    }

    public Connection getConnection(Map<String, Serializable> parameters) {
        loadTypes();
        return new APPConnection(this);
    }

    public void addCollection(String type, String href) {
        collections.put(type, href);
    }

    public RepositoryInfo getInfo() {
        return info;
    }

    public Type getType(String typeId) {
        loadTypes();
        return typeRegistry.get(typeId);
    }

    public List<Type> getTypes(String typeId,
            boolean returnPropertyDefinitions, int maxItems, int skipCount,
            boolean[] hasMoreItems) {
        loadTypes();
        throw new UnsupportedOperationException("Not yet Implemented");
    }

    public Collection<Type> getTypes(String typeId,
            boolean returnPropertyDefinitions) {
        loadTypes();
        throw new UnsupportedOperationException("Not yet Implemented");
    }

    public String getRelationshipName() {
        return info.getRelationshipName();
    }

    public String getCollectionHref(String type) {
        return collections.get(type);
    }

    /** type API */

    public void addType(APPType type) {
        typeRegistry.put(type.getId(), type);
    }

    protected void loadTypes() {
        if (typeRegistry == null) {
            try {
                String href = getCollectionHref(CMIS.COL_TYPES_CHILDREN);
                if (href == null) {
                    throw new IllegalArgumentException(
                            "Invalid CMIS repository. No types children collection defined");
                }
                // TODO lazy load property definition
                Request req = new Request(href + "?includePropertyDefinitions=true");
                Response resp = cm.getConnector().get(req);
                if (!resp.isOk()) {
                    throw new ContentManagerException(
                            "Remote server returned error code: "
                                    + resp.getStatusCode());
                }
                InputStream in = resp.getStream();
                try {
                    typeRegistry = TypeFeedReader.INSTANCE.read(
                            new ReadContext(this), in);
                } finally {
                    in.close();
                }
            } catch (Exception e) { // TODO how to handle exceptions?
                throw new RuntimeException(
                        "Failed to load repository types for " + getName(), e);
            }
        }
    }

    /*
     * Subclasses should override this.
     */
    public <T> T getExtension(Class<T> clazz) {
        return null;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + '(' + getName() + ')';
    }

}
