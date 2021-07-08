package com.example.restservice;


import com.microsoft.graph.models.PlannerTask;

import com.microsoft.graph.models.PlannerTaskDetails;
import com.microsoft.graph.options.HeaderOption;
import com.microsoft.graph.options.Option;
import com.microsoft.graph.requests.GraphServiceClient;


import com.microsoft.graph.requests.PlannerTaskCollectionPage;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.*;
import java.util.concurrent.CompletableFuture;


@RestController
@RequestMapping("/task")
public class TaskController {

    @PostMapping(value = "/add")
    @ResponseBody
    public ResponseEntity<?> addTask(@RequestBody PlannerTask task) {
        if(AuthConfig.graphClient == null)
            AuthConfig.graphClient  = AuthConfig.getClient();
        try{
            AuthConfig.graphClient.me().planner().tasks()
                    .buildRequest()
                    .post(task);
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(task);
        }catch (Exception e){
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(e.getMessage());
        }
    }

    @PostMapping(value = "/update")
    @ResponseBody
    public ResponseEntity<?> updateTask(@RequestBody PlannerTask task, @RequestParam("taskID") String taskID, @RequestHeader("If-Match") String eTag) {
        if(AuthConfig.graphClient == null)
            AuthConfig.graphClient  = AuthConfig.getClient();
        try{
            LinkedList<Option> requestOptions = new LinkedList<Option>();
            requestOptions.add(new HeaderOption("Content-type", "application/json"));
            requestOptions.add(new HeaderOption("Prefer", "return=representation"));
            requestOptions.add(new HeaderOption("If-Match", eTag));
            PlannerTask plannerTask = AuthConfig.graphClient.me().planner().tasks(taskID)
                    .buildRequest(requestOptions)
                    .patch(task);
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(plannerTask);
        }catch (Exception e){
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(e.getMessage());
        }
    }
    @PostMapping(value = "/delete")
    @ResponseBody
    public ResponseEntity<?> deleteTask(@RequestParam("taskID") String taskID, @RequestHeader("If-Match") String eTag) {
        if(AuthConfig.graphClient == null)
            AuthConfig.graphClient  = AuthConfig.getClient();
        try{
            LinkedList<Option> requestOptions = new LinkedList<Option>();
            requestOptions.add(new HeaderOption("Prefer", "return=representation"));
            requestOptions.add(new HeaderOption("If-Match", eTag));


            PlannerTask plannerTask = AuthConfig.graphClient.planner().tasks(taskID)
                    .buildRequest( requestOptions )
                    .delete();
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(plannerTask);
        }catch (Exception e){
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(e.getMessage());
        }
    }


    @GetMapping("/gettaskbyuser")
    public ResponseEntity<?> getAllTasksByUser(@RequestParam("userID")  String userID){
        if(AuthConfig.graphClient == null)
            AuthConfig.graphClient  = AuthConfig.getClient();
        try {
            PlannerTaskCollectionPage plannerTaskCollectionPage =  AuthConfig.graphClient.users(userID).planner().tasks()
                    .buildRequest()
                    .get();
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(plannerTaskCollectionPage);
        }catch (Exception e){
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(e.getMessage());
        }

    }

    @GetMapping("/getmytask")
    public ResponseEntity<?> getMyTasks(){
        if(AuthConfig.graphClient == null)
            AuthConfig.graphClient  = AuthConfig.getClient();
        try {
            PlannerTaskCollectionPage plannerTaskCollectionPage =  AuthConfig.graphClient.me().planner().tasks()
                    .buildRequest()
                    .get();
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(plannerTaskCollectionPage);
        }catch (Exception e){
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(e.getMessage());
        }

    }

    @GetMapping("/gettaskbyid")
    public ResponseEntity<?> getTaskById(@RequestParam("taskID")  String taskID){
        if(AuthConfig.graphClient == null)
            AuthConfig.graphClient  = AuthConfig.getClient();
        try {
            PlannerTask plannerTask =  AuthConfig.graphClient.planner().tasks(taskID)
                    .buildRequest()
                    .get();
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(plannerTask);
        }catch (Exception e){
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(e.getMessage());
        }
    }

    @GetMapping("/gettaskdetailbyid")
    public ResponseEntity<?> getDetailTaskById(@RequestParam("taskID")  String taskID){
        if(AuthConfig.graphClient == null)
            AuthConfig.graphClient  = AuthConfig.getClient();
        try {
            PlannerTaskDetails plannerTaskDetails =  AuthConfig.graphClient.planner().tasks(taskID).details()
                    .buildRequest()
                    .get();
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(plannerTaskDetails);
        }catch (Exception e){
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(e.getMessage());
        }
    }

    @PostMapping(value = "/updateDetail")
    @ResponseBody
    public ResponseEntity<?> updateTaskDetail(@RequestBody PlannerTaskDetails taskDetails, @RequestParam("taskID") String taskID) {
        if(AuthConfig.graphClient == null)
            AuthConfig.graphClient  = AuthConfig.getClient();
        try{
            AuthConfig.graphClient.me().planner().tasks(taskID).details()
                    .buildRequest()
                    .patch(taskDetails);
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(taskDetails);
        }catch (Exception e){
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(e.getMessage());
        }
    }

    @GetMapping("/getplantasks")
    public ResponseEntity<?> getTasksByPlan(@RequestParam("planID")  String planID){
        if(AuthConfig.graphClient == null)
            AuthConfig.graphClient  = AuthConfig.getClient();
        try {

            PlannerTaskCollectionPage tasks = AuthConfig.graphClient.planner().plans(planID).tasks()
                    .buildRequest()
                    .get();
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(tasks);
        }catch (Exception e){
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(e.getMessage());
        }
    }



    @GetMapping("/deletemytask")
    public ResponseEntity<?> deleteMyTasks(@RequestParam("taskID")  String taskID){
        if(AuthConfig.graphClient == null)
            AuthConfig.graphClient  = AuthConfig.getClient();
        try {
            LinkedList<Option> requestOptions = new LinkedList<Option>();
            requestOptions.add(new HeaderOption("If-Match", "W/\"JzEtVGFzayAgQEBAQEBAQEBAQEBAQEBAWCc=\""));

            PlannerTask plannerTask = AuthConfig.graphClient.planner().tasks("{id}")
                    .buildRequest( requestOptions )
                    .delete();

            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(plannerTask);
        }catch (Exception e){
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(e.getMessage());
        }
    }


}
