package de.hpi.oryxengine.factory.process;


import java.util.Set;

import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.hpi.oryxengine.IdentityService;
import de.hpi.oryxengine.ServiceFactory;
import de.hpi.oryxengine.activity.Activity;
import de.hpi.oryxengine.activity.impl.EndActivity;
import de.hpi.oryxengine.activity.impl.HumanTaskActivity;
import de.hpi.oryxengine.activity.impl.NullActivity;
import de.hpi.oryxengine.allocation.Task;
import de.hpi.oryxengine.factory.worklist.TaskFactory;
import de.hpi.oryxengine.loadgenerator.PseudoHumanJob;
import de.hpi.oryxengine.process.definition.NodeParameter;
import de.hpi.oryxengine.process.definition.NodeParameterImpl;
import de.hpi.oryxengine.process.definition.ProcessBuilderImpl;
import de.hpi.oryxengine.process.structure.Node;
import de.hpi.oryxengine.resource.AbstractParticipant;
import de.hpi.oryxengine.resource.IdentityBuilder;
import de.hpi.oryxengine.resource.Participant;
import de.hpi.oryxengine.resource.Role;
import de.hpi.oryxengine.routing.behaviour.incoming.impl.SimpleJoinBehaviour;
import de.hpi.oryxengine.routing.behaviour.outgoing.impl.TakeAllSplitBehaviour;

/**
 * A factory for creating ExampleProcessToken objects.
 * These objects just have 2 add Number activities.
 */
public class HumanTaskProcessDeployer extends AbstractProcessDeployer {
    
    private static final String JANNIK = "Jannik";
    private static final String TOBI = "Tobi";
    private static final String LAZY = "lazy guy";
    private static final String ROLE = "DUMMIES";
    private static final String JOBGROUP = "dummy";
    private IdentityBuilder identityBuilder;
    
    private IdentityService identityService;
    
    public static final String PARTICIPANT_KEY = "Participant";
    
    /** an array with the waiting times of the different pseudo humans. */ 
    public static final int[] WAITING_TIME = {1000, 1000, 3000};
    
    private Scheduler scheduler;
    
    /** The node1. */
    private Node node1;
    
    /** The node2. */
    private Node node2;
    
//    /** The builder. */
//    private ProcessBuilder builder;
    
    /** The start node. */
    private Node startNode;
    
    private Task task = null;
    
    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    /**
     * Instantiates a new example process token factory.
     * @throws SchedulerException 
     */
    public HumanTaskProcessDeployer() throws SchedulerException {
        
        identityService = ServiceFactory.getIdentityService();
        builder = new ProcessBuilderImpl();
        identityBuilder = identityService.getIdentityBuilder();

    }
    
    /**
     * Initializes the nodes.
     */
    public void initializeNodes() {
        NodeParameter param = new NodeParameterImpl(new NullActivity(), new SimpleJoinBehaviour(),
            new TakeAllSplitBehaviour());
        param.makeStartNode(true);
        startNode = builder.createNode(param);
        param.makeStartNode(false);
        
        // Create the task
        task = TaskFactory.createParticipantTask((AbstractParticipant) identityService.getParticipants().toArray()[0]);
        Activity activity  = new HumanTaskActivity(task);
        param.setActivity(activity);
        
        node1 = builder.createNode(param);
        
        // Create the next task
        task = TaskFactory.createParticipantTask((AbstractParticipant) identityService.getParticipants().toArray()[1]);
        Activity activity2  = new HumanTaskActivity(task);
        param.setActivity(activity2);
        
        node2 = builder.createNode(param);
        builder.createTransition(startNode, node1).createTransition(node1, node2);
        
        param.setActivity(new EndActivity());
        Node endNode = builder.createNode(param);
        builder.createTransition(node2, endNode);

    }
    
    /**
     * Creates our dummy participants with a common role.
     * Those are the ones that will claim and complete activity within a time interval that is determined within
     * the schedule dummy participants method.
     */
    public void createAutomatedParticipants() {
       
        Participant jannik = (Participant) identityBuilder.createParticipant(JANNIK);
        Participant tobi = (Participant) identityBuilder.createParticipant(TOBI);
        Participant lazy = (Participant) identityBuilder.createParticipant(LAZY);
        Role role = (Role) identityBuilder.createRole(ROLE);
        identityBuilder
            .participantBelongsToRole(jannik.getID(), role.getID())
            .participantBelongsToRole(tobi.getID(), role.getID())
            .participantBelongsToRole(lazy.getID(), role.getID());
        
    }
    
    /**
     * Schedule the dummy participants with a given time. A quartz scheduler is used to schedule them.
     *
     * @throws SchedulerException the scheduler exception
     */
    public void scheduleDummyParticipants() throws SchedulerException {
        
        // Create the quartz scheduler
        final SchedulerFactory factory = new org.quartz.impl.StdSchedulerFactory();
        this.scheduler = factory.getScheduler();
        this.scheduler.start();
        
        // Schdule the jobs of our participants
        Set<AbstractParticipant> participants = ServiceFactory.getIdentityService().getParticipants();
        int i = 0;
        for (AbstractParticipant participant : participants) {
            
            JobDetail jobDetail = new JobDetail(
                participant.getName(),
                JOBGROUP,
                PseudoHumanJob.class);
            JobDataMap data = jobDetail.getJobDataMap();
            data.put(PARTICIPANT_KEY, participant);

            Trigger trigger = new SimpleTrigger(participant.getID().toString(), -1, WAITING_TIME[i++]);
            
            try {
                this.scheduler.scheduleJob(jobDetail, trigger);
            } catch (SchedulerException se) {
                logger.error("Failed scheduling of event manager job", se);
            }
            
        }
        
    }
    
    /**
     * Really creates Pseudo Humans. @see scheduleDummyParticipants
     * {@inheritDoc}
     */
    @Override
    public void createPseudoHuman() {
        createAutomatedParticipants();
        try {
            scheduleDummyParticipants();
        } catch (SchedulerException e) {
            logger.error("Scheduler Exception when trying to schedule dummy Participants", e);
        }
    }

}