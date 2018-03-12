package modules.sms;

import actors.sms.SingleSmsActor;
import com.google.inject.AbstractModule;
import play.libs.akka.AkkaGuiceSupport;

public class SingleSmsAkkaModule extends AbstractModule implements AkkaGuiceSupport {

    @Override
    protected void configure() {

        bindActor(SingleSmsActor.class, "single-sms-actor");
    }
}
