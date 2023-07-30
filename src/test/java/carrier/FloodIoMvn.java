package carrier;

import io.gatling.javaapi.core.Choice;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import static carrier.requestsjava.FloodIORequestsJava.*;
import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;

public class FloodIoMvn extends Simulation {

    //String environment = System.getenv("environment");
    //String environment = System.getenv("demo_environment");
    String environment = "https://training.flooded.io";
    //   int ramp_users = 5;
    int ramp_users = Integer.getInteger("ramp_users", 5);
 //   int ramp_duration = 120;
    int ramp_duration = Integer.getInteger("ramp_duration", 30);
 //   int duration = 120;
    int duration = Integer.getInteger("duration", 60);


    HttpProtocolBuilder webProtocol = http
            .baseUrl(environment)
            .disableCaching()
            .disableFollowRedirect();

    ScenarioBuilder flood_io = scenario("flood_io")
            .tryMax(10).on(
                    exec(Step1GET)
                            .exec(Step1POST)
                            .exec(Step2GET)
                            .exec(Step2POST)
                            .exec(Step3GET)
                            .exec(Step3POST)
                            .exec(Step4GET)
                            .exec(Step4POST)
                            .exec(dataJSON)
                            .exec(Step5GET)
                            .exec(Step5POST)
                            .randomSwitch()
                            .on(Choice.withWeight(60.0, FinalStep), Choice.withWeight(40.0, failedFinalStep))
                            .exitHereIfFailed());


    {
        setUp(flood_io.injectOpen(rampUsers(ramp_users).during(ramp_duration)).protocols(webProtocol));
    }
}
