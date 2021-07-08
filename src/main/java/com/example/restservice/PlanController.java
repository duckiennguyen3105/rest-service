package com.example.restservice;


import com.microsoft.graph.models.PlannerPlan;
import com.microsoft.graph.requests.PlannerBucketCollectionPage;
import com.microsoft.graph.requests.PlannerPlanCollectionPage;
import com.microsoft.graph.requests.PlannerTaskCollectionPage;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/plan")
public class PlanController {
    @GetMapping("/getgroupplan")
    public ResponseEntity<?> getGroupPlans(@RequestParam("groupID")  String groupID){
        if(AuthConfig.graphClient == null)
            AuthConfig.graphClient  = AuthConfig.getClient();
        try {

            PlannerPlanCollectionPage plans = AuthConfig.graphClient.groups(groupID).planner().plans()
                    .buildRequest()
                    .get();
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(plans);
        }catch (Exception e){
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(e.getMessage());
        }
    }
    @GetMapping("/getplanbyid")
    public ResponseEntity<?> getPlanByID(@RequestParam("planID")  String planID){
        if(AuthConfig.graphClient == null)
            AuthConfig.graphClient  = AuthConfig.getClient();
        try {

            PlannerPlan plannerPlan = AuthConfig.graphClient.planner().plans(planID)
                    .buildRequest()
                    .get();
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(plannerPlan);
        }catch (Exception e){
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(e.getMessage());
        }
    }

    @GetMapping("/getplanbuckets")
    public ResponseEntity<?> getTasksByPlan(@RequestParam("planID")  String planID){
        if(AuthConfig.graphClient == null)
            AuthConfig.graphClient  = AuthConfig.getClient();
        try {

            PlannerBucketCollectionPage buckets = AuthConfig.graphClient.planner().plans(planID).buckets()
                    .buildRequest()
                    .get();
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(buckets);
        }catch (Exception e){
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(e.getMessage());
        }
    }

}
