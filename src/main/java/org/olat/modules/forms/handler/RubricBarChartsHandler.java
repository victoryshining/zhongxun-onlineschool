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
package org.olat.modules.forms.handler;

import java.util.List;

import org.olat.core.gui.UserRequest;
import org.olat.core.gui.control.Controller;
import org.olat.core.gui.control.WindowControl;
import org.olat.modules.forms.EvaluationFormSessionRef;
import org.olat.modules.forms.model.xml.Rubric;
import org.olat.modules.forms.ui.ReportHelper;
import org.olat.modules.forms.ui.RubricSlidersBarChartsController;
import org.olat.modules.forms.ui.RubricTotalBarChartsController;
import org.olat.modules.forms.ui.model.EvaluationFormControllerReportElement;
import org.olat.modules.forms.ui.model.EvaluationFormReportElement;
import org.olat.modules.portfolio.ui.editor.PageElement;

/**
 * 
 * Initial date: 25.05.2018<br>
 * @author uhensler, urs.hensler@frentix.com, http://www.frentix.com
 *
 */
public class RubricBarChartsHandler implements EvaluationFormReportHandler {

	private boolean totals;
	
	public RubricBarChartsHandler(boolean totals) {
		super();
		this.totals = totals;
	}

	@Override
	public String getType() {
		return "rubricbarcharts";
	}

	@Override
	public EvaluationFormReportElement getReportElement(UserRequest ureq, WindowControl windowControl, PageElement element,
			List<? extends EvaluationFormSessionRef> sessions, ReportHelper reportHelper) {
		if (element instanceof Rubric) {
			Rubric rubric = (Rubric) element;
			Controller ctrl;
			if (totals) {
				ctrl = new RubricTotalBarChartsController(ureq, windowControl, rubric, sessions);
			} else {
				ctrl = new RubricSlidersBarChartsController(ureq, windowControl, rubric, sessions);
			}
			return new EvaluationFormControllerReportElement(ctrl);
		}
		return null;
	}

}