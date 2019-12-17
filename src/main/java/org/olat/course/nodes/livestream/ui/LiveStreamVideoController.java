/**
 * <a href="http://www.openolat.org">
 * OpenOLAT - Online Learning and Training</a><br>
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); <br>
 * you may not use this file except in compliance with the License.<br>
 * You may obtain a copy of the License at the
 * <a href="http://www.apache.org/licenses/LICENSE-2.0">Apache homepage</a>
 * <p>
 * Unless required by applicable law or agreed to in writing,<br>
 * software distributed under the License is distributed on an "AS IS" BASIS, <br>
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. <br>
 * See the License for the specific language governing permissions and <br>
 * limitations under the License.
 * <p>
 * Initial code contributed and copyrighted by<br>
 * frentix GmbH, http://www.frentix.com
 * <p>
 */
package org.olat.course.nodes.livestream.ui;

import java.util.ArrayList;
import java.util.List;

import org.olat.core.dispatcher.mapper.MapperService;
import org.olat.core.dispatcher.mapper.manager.MapperKey;
import org.olat.core.gui.UserRequest;
import org.olat.core.gui.components.Component;
import org.olat.core.gui.components.velocity.VelocityContainer;
import org.olat.core.gui.control.Event;
import org.olat.core.gui.control.WindowControl;
import org.olat.core.gui.control.controller.BasicController;
import org.olat.core.util.CodeHelper;
import org.olat.core.util.StringHelper;
import org.olat.core.util.UserSession;
import org.olat.course.nodes.livestream.LiveStreamEvent;
import org.olat.course.nodes.livestream.paella.PaellaFactory;
import org.olat.course.nodes.livestream.paella.PaellaMapper;
import org.olat.course.nodes.livestream.paella.Streams;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 
 * Initial date: 29 May 2019<br>
 * @author uhensler, urs.hensler@frentix.com, http://www.frentix.com
 *
 */
public class LiveStreamVideoController extends BasicController {

	private final VelocityContainer mainVC;
	
	private final List<MapperKey> mappers = new ArrayList<>();
	private String url;
	
	@Autowired
	private MapperService mapperService;

	protected LiveStreamVideoController(UserRequest ureq, WindowControl wControl) {
		super(ureq, wControl);
		mainVC = createVelocityContainer("video");
		updateUI(ureq.getUserSession());
		putInitialPanel(mainVC);
	}
	
	public void setEvent(UserSession usess, LiveStreamEvent event) {
		String newUrl = event != null? event.getLiveStreamUrl(): null;
		if (newUrl == null || !newUrl.equalsIgnoreCase(url)) {
			url = newUrl;
			updateUI(usess);
		}
	}

	private void updateUI(UserSession usess) {
		if (StringHelper.containsNonWhitespace(url)) {
			mainVC.contextPut("id", CodeHelper.getRAMUniqueID());
			Streams streams = PaellaFactory.createStreams(url);
			PaellaMapper paellaMapper = new PaellaMapper(streams);
			MapperKey mapperKey = mapperService.register(usess, paellaMapper);
			mappers.add(mapperKey);
			String baseURI = mapperKey.getUrl();
			mainVC.contextPut("baseURI", baseURI);
		}	else {
			mainVC.contextRemove("id");
		}
	}

	@Override
	protected void event(UserRequest ureq, Component source, Event event) {
		//
	}

	@Override
	protected void doDispose() {
		mapperService.cleanUp(mappers);
	}

}
