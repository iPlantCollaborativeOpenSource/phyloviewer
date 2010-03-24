/**
 * 
 */
package org.iplantc.iptol.client.JobConfiguration.contrast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.iplantc.iptol.client.IptolDisplayStrings;
import org.iplantc.iptol.client.JobConfiguration.Card;
import org.iplantc.iptol.client.JobConfiguration.DataSelectedEvent;
import org.iplantc.iptol.client.JobConfiguration.DataSelectedEventHandler;
import org.iplantc.iptol.client.JobConfiguration.EnableStepEvent;
import org.iplantc.iptol.client.JobConfiguration.EnableStepEventHandler;
import org.iplantc.iptol.client.JobConfiguration.JobParams;
import org.iplantc.iptol.client.JobConfiguration.JobStep;
import org.iplantc.iptol.client.JobConfiguration.JobToolBarSaveClickEvent;
import org.iplantc.iptol.client.JobConfiguration.JobToolBarSaveClickEventHandler;
import org.iplantc.iptol.client.JobConfiguration.JobView;
import org.iplantc.iptol.client.JobConfiguration.MessageNotificationEvent;
import org.iplantc.iptol.client.JobConfiguration.MessageNotificationEventHandler;
import org.iplantc.iptol.client.JobConfiguration.NavButtonClickEvent;
import org.iplantc.iptol.client.JobConfiguration.NavButtonEventClickEventHandler;
import org.iplantc.iptol.client.JobConfiguration.MessageNotificationEvent.MessageType;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.layout.CardLayout;
import com.extjs.gxt.ui.client.widget.tips.Tip;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Timer;

/**
 * @author sriram Builds the cards in the wizard for this job. Acts as a
 *         controller in initializing the cards and setting the appropriate card
 *         for each step.
 */
public class IndepdentContrastJobView implements JobView {

	private ContentPanel panel;
	private CardLayout layout;

	private Card selecttreesGrid;
	private Card selecttraitGrid;
	private Card reconcile;
	private Card selectparams;
	private Card confirm;
	private HandlerManager eventbus;
	private JobParams params;

	private ArrayList<JobStep> steps;

	private IptolDisplayStrings displayStrings = (IptolDisplayStrings) GWT
			.create(IptolDisplayStrings.class);

	/**
	 * Create a new IndepdentContrastJobView
	 * 
	 * @param eventbus
	 *            event bus for handling events
	 */
	// this must take a job config object from workspace service
	public IndepdentContrastJobView(HandlerManager eventbus) {
		panel = new ContentPanel();
		layout = new CardLayout();
		this.eventbus = eventbus;
		params = new JobParams();
		removeHandlers();
		registerEventHandlers();
		
	}

	/**
	 * get the wizard for configuring this job
	 * 
	 */
	@Override
	public ContentPanel getWizard() {
		panel.setFrame(true);
		panel.setHeading(displayStrings.independentcontrast());
		panel.setButtonAlign(HorizontalAlignment.CENTER);
		panel.setLayout(layout);

		final LayoutContainer c1 = new LayoutContainer();
		selecttreesGrid = new SelectTrees(0, eventbus);
		c1.add(selecttreesGrid.assembleView());
		panel.add(c1);

		final LayoutContainer c2 = new LayoutContainer();
		selecttraitGrid = new SelectTraits(1, eventbus);
		c2.add(selecttraitGrid.assembleView());
		panel.add(c2);

		final LayoutContainer c3 = new LayoutContainer();
		reconcile = new Reconcile(2, eventbus);
		c3.add(reconcile.assembleView());
		panel.add(c3);

		final LayoutContainer c4 = new LayoutContainer();
		selectparams = new SelectOptionalParams(3, eventbus);
		c4.add(selectparams.assembleView());
		panel.add(c4);

		final LayoutContainer c5 = new LayoutContainer();
		confirm = new ConfirmJobDetails(4, eventbus);
		c5.add(confirm.assembleView());
		panel.add(c5);

		layout.setActiveItem(panel.getItem(0));
		return panel;
	}
	
	private void registerEventHandlers() {
		eventbus.addHandler(NavButtonClickEvent.TYPE,
				new NavButtonEventClickEventHandler() {
					@Override
					public void onClick(NavButtonClickEvent navButton) {
						JobStep step = navButton.getStep();
						setJobStep(step.getStepno());
					}
				});

		eventbus.addHandler(DataSelectedEvent.TYPE,
				new DataSelectedEventHandler() {
					@Override
					public void onDataSelected(DataSelectedEvent dse) {
						if (dse.isSelected()) {
							HashMap<String, Object> param = dse.getData();
							// collect data from the step if any
							if (param != null) {
								Iterator<String> it = param.keySet().iterator();
								while (it.hasNext()) {
									String key = (String) it.next();
									params.add(key, param.get(key));
								}
							}

							// update step status
							for (JobStep step : steps) {
								if (step.getStepno() == dse.getStep()) {
									step.setComlpete(true);
								}
							}

						} else {
							for (JobStep step : steps) {
								if (step.getStepno() == dse.getStep()) {
									step.setComlpete(false);
								}
							}
						}

						// this should come from meta data - dependent steps
						if (steps.get(0).isComlpete()
								&& steps.get(1).isComlpete()) {
							EnableStepEvent event = new EnableStepEvent(2, true);
							eventbus.fireEvent(event);
						} else {
							EnableStepEvent event = new EnableStepEvent(2,
									false);
							eventbus.fireEvent(event);
						}

						if (steps.get(0).isComlpete()
								&& steps.get(1).isComlpete()
								&& steps.get(2).isComlpete()) {
							EnableStepEvent event = new EnableStepEvent(4, true);
							eventbus.fireEvent(event);
						} else {
							EnableStepEvent event = new EnableStepEvent(4,
									false);
							eventbus.fireEvent(event);
						}

					}
				});

		eventbus.addHandler(MessageNotificationEvent.TYPE,
				new MessageNotificationEventHandler() {
					@Override
					public void onMessage(MessageNotificationEvent mne) {
						if (mne.getMsgType() == MessageType.ERROR) {
							MessageBox.alert(displayStrings.error(), mne
									.getMsg().toString(), null);
						} else {
							final Tip t = new Tip();
							t.setHeading(mne.getMsgType().name());
							t.setHeight(30);
							t.setWidth(panel.getWidth());
							t.addText(mne.getMsg());
							t.setClosable(true);
							t.showAt(panel.getPosition(false));
							Timer timer = new Timer() {
								public void run() {
									t.hide();
								}
							};

							timer.schedule(5000);
						}
					}
				});
		
		
		eventbus.addHandler(JobToolBarSaveClickEvent.TYPE, new JobToolBarSaveClickEventHandler() {
			@Override
			public void onSave(JobToolBarSaveClickEvent saveEvent) {
				
			}
		});

	}

	/**
	 * clear handlers before adding again
	 */
	private void removeHandlers() {
		int count = eventbus.getHandlerCount(NavButtonClickEvent.TYPE);
		
		for (int i = 0 ; i < count ; i++) {
			NavButtonEventClickEventHandler e = eventbus.getHandler(NavButtonClickEvent.TYPE, i);
			eventbus.removeHandler(NavButtonClickEvent.TYPE, e);
		}
		
		count = eventbus.getHandlerCount(DataSelectedEvent.TYPE);
		
		for (int k = 0 ; k<count ; k++) {
			DataSelectedEventHandler e = eventbus.getHandler(DataSelectedEvent.TYPE,k);
			eventbus.removeHandler(DataSelectedEvent.TYPE, e);
		}
		
		count = eventbus.getHandlerCount(MessageNotificationEvent.TYPE);
		
		for (int j = 0 ; j<count;j++) {
			MessageNotificationEventHandler e = eventbus.getHandler(MessageNotificationEvent.TYPE, j);
			eventbus.removeHandler(MessageNotificationEvent.TYPE, e);
		}
		
	}
	/**
	 * set the data and view for the current step
	 */
	@Override
	public void setJobStep(int step) {

		if (step == selecttreesGrid.getStep()) {
			selecttreesGrid.setJobParams(params);
		} else if (step == selecttraitGrid.getStep()) {
			selecttraitGrid.setJobParams(params);
		} else if (step == selectparams.getStep()) {
			selectparams.setJobParams(params);
		} else if (step == reconcile.getStep()) {
			reconcile.setJobParams(params);
		} else {
			confirm.setJobParams(params);
		}

		layout.setActiveItem(panel.getItem(step));
	}

	/**
	 * get the steps involved in configuring this job. This should come from
	 * meta-data service
	 * 
	 * @return
	 */
	@Override
	public ArrayList<JobStep> getJobConfigSteps() {
		steps = new ArrayList<JobStep>();
		steps.add(new JobStep(0, "Select Tree(s)", true));
		steps.add(new JobStep(1, "Select Traits", true));
		steps.add(new JobStep(2, "Reconcile", false));
		steps.add(new JobStep(3, "Select Params", true));
		steps.add(new JobStep(4, "Confirm", false));
		return steps;
	}

	public JobParams getParams() {
		return params;
	}

}