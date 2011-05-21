package org.blackpanther.ecosystem.factory.generator.random;

import org.blackpanther.ecosystem.BehaviorManager;
import org.blackpanther.ecosystem.Configuration;
import org.blackpanther.ecosystem.behaviour.DraughtsmanBehaviour;
import org.blackpanther.ecosystem.factory.generator.RandomProvider;

/**
 * @author MACHIZAUD Andréa
 * @version 5/22/11
 */
public class BehaviorProvider
        extends RandomProvider<BehaviorManager> {

    private static final BehaviorManager[] possibleBehaviours = new BehaviorManager[]{
            DraughtsmanBehaviour.getInstance()
//            , PredatorBehaviour.getInstance()
//            , PreyBehaviour.getInstance()
    };

    private BehaviorProvider(){}

    private static class BehaviorProviderHolder {
        private static final BehaviorProvider instance =
            new BehaviorProvider();
    }

    public static BehaviorProvider getInstance(){
        return BehaviorProviderHolder.instance;
    }

    @Override
    public BehaviorManager getValue() {
        return possibleBehaviours[Configuration.Configuration.getRandom().nextInt(possibleBehaviours.length)];
    }
}
