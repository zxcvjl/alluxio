/*
 * Licensed to the University of California, Berkeley under one or more contributor license
 * agreements. See the NOTICE file distributed with this work for additional information regarding
 * copyright ownership. The ASF licenses this file to You under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License. You may obtain a
 * copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package tachyon.web;

import java.io.IOException;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.tuple.ImmutablePair;

import tachyon.Constants;
import tachyon.conf.TachyonConf;
import tachyon.master.MasterInfo;

/**
 * Servlet that provides data for displaying the system's configuration.
 */
public final class WebInterfaceConfigurationServlet extends HttpServlet {
  private static final long serialVersionUID = 2134205675393443914L;
  private static final String TACHYON_CONF_PREFIX = "tachyon";

  private final transient MasterInfo mMasterInfo;
  private final TachyonConf mTachyonConf;

  public WebInterfaceConfigurationServlet(MasterInfo masterInfo) {
    mMasterInfo = masterInfo;
    mTachyonConf = new TachyonConf();
  }

  /**
   * Populates attributes before redirecting to a jsp.
   * 
   * @param request The HttpServletRequest object
   * @param response The HttpServletReponse object
   * @throws ServletException
   * @throws IOException
   */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    request.setAttribute("whitelist", mMasterInfo.getWhiteList());
    request.setAttribute("configuration", getSortedProperties());

    getServletContext().getRequestDispatcher("/configuration.jsp").forward(request, response);
  }

  private SortedSet<ImmutablePair<String, String>> getSortedProperties() {
    TreeSet<ImmutablePair<String, String>> rtn = new TreeSet<ImmutablePair<String, String>>();
    for (Map.Entry<Object, Object> entry : mTachyonConf.getInternalProperties().entrySet()) {
      String key = entry.getKey().toString();
      if (key.startsWith(TACHYON_CONF_PREFIX)) {
        rtn.add(new ImmutablePair<String, String>(key, entry.getValue().toString()));
      }
    }
    return rtn;
  }
}
