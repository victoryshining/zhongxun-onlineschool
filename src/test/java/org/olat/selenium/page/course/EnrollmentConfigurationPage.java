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
package org.olat.selenium.page.course;

import java.util.List;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.junit.Assert;
import org.olat.selenium.page.graphene.OOGraphene;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Drive the configuration of the enrolment course element.
 * 
 * Initial date: 09.03.2015<br>
 * @author srosse, stephane.rosse@frentix.com, http://www.frentix.com
 *
 */
public class EnrollmentConfigurationPage {
	
	@Drone
	private WebDriver browser;
	
	public EnrollmentConfigurationPage() {
		//
	}
	
	public EnrollmentConfigurationPage(WebDriver browser) {
		this.browser = browser;
	}
	
	public EnrollmentConfigurationPage selectConfiguration() {
		By configBy = By.className("o_sel_course_en");
		return selectTab(configBy);
	}
	
	public EnrollmentConfigurationPage selectBusinessGroups() {
		By createGroupBy = By.cssSelector("a.o_sel_course_en_choose_group");
		browser.findElement(createGroupBy).click();
		OOGraphene.waitBusy(browser);
		
		By checkGroupsBy = By.cssSelector("div.modal-body input[type='checkbox'][name='entries']");
		List<WebElement> checkGroupEls = browser.findElements(checkGroupsBy);
		for(WebElement checkGroupEl:checkGroupEls) {
			checkGroupEl.click();
			OOGraphene.waitBusy(browser);
		}
		
		By selectBy = By.cssSelector("div.modal-body div.o_button_group button.btn.btn-primary");
		browser.findElement(selectBy).click();
		OOGraphene.waitBusy(browser);
		return this;
	}
	
	/**
	 * Open the popup to create a business group.
	 * @return
	 */
	public EnrollmentConfigurationPage createBusinessGroup(String name, String description,
			int maxParticipants, boolean waitingList, boolean auto) {
		By createGroupBy = By.cssSelector("a.o_sel_course_en_create_group");
		browser.findElement(createGroupBy).click();
		OOGraphene.waitBusy(browser);
		
		//fill the form
		By nameBy = By.cssSelector(".o_sel_group_edit_title input[type='text']");
		browser.findElement(nameBy).sendKeys(name);
		OOGraphene.tinymce(description, browser);
		
		By maxParticipantBy = By.cssSelector(".o_sel_group_edit_max_members input[type='text']");
		browser.findElement(maxParticipantBy).sendKeys(Integer.toString(maxParticipants));
		
		if(waitingList) {
			By waitingListBy = By.cssSelector(".o_sel_group_edit_waiting_list input[type='checkbox']");
			browser.findElement(waitingListBy).click();
			OOGraphene.waitBusy(browser);
		}
		if(auto) {
			By autoBy = By.cssSelector(".o_sel_group_edit_auto_close_ranks input[type='checkbox']");
			browser.findElement(autoBy).click();
			OOGraphene.waitBusy(browser);
		}
		
		//save
		By submitBy = By.cssSelector(".o_sel_group_edit_group_form button.btn-primary");
		WebElement submitButton = browser.findElement(submitBy);
		submitButton.click();
		OOGraphene.waitBusy(browser);
				
		return this;
	}
	
	private EnrollmentConfigurationPage selectTab(By tabBy) {
		List<WebElement> tabLinks = browser.findElements(CourseEditorPageFragment.navBarNodeConfiguration);

		boolean found = false;
		a_a:
		for(WebElement tabLink:tabLinks) {
			tabLink.click();
			OOGraphene.waitBusy(browser);
			List<WebElement> chooseRepoEntry = browser.findElements(tabBy);
			if(chooseRepoEntry.size() > 0) {
				found = true;
				break a_a;
			}
		}

		Assert.assertTrue("Found the tab", found);
		return this;
	}

}
