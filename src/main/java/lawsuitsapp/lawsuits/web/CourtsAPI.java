package lawsuitsapp.lawsuits.web;


import lawsuitsapp.lawsuits.async.AsyncCourtsService;
import lawsuitsapp.lawsuits.model.Court;
import lawsuitsapp.lawsuits.model.exceptions.CourtNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping(path = "/courts")
public class CourtsAPI {

    AsyncCourtsService asyncCourtsService;

    public CourtsAPI(AsyncCourtsService asyncCourtsService){
        this.asyncCourtsService = asyncCourtsService;
    }

    @GetMapping
    public List<Court> getAllCourtsFromRepo() throws ExecutionException, InterruptedException {
        List<Court> result = asyncCourtsService.getAllCourtsAsync().get();
        return result;
    }

    @GetMapping("/{id}")
    public Court getCourtByIdFromRepo(@PathVariable("id")int id) throws CourtNotFoundException, InterruptedException, ExecutionException {

        CompletableFuture<Court> cfCourt = asyncCourtsService.getCourtByIdAsync(id);


        System.out.println("~~~~~~~~~~~~~~from API thread: "+Thread.currentThread().getName());

        Court c = cfCourt.get();
        return c;
    }


    @PostMapping
    public void addCourtToRepo(@RequestParam("name")String name,
                               @RequestParam("type")String type,
                               @RequestParam("city")String city,
                               @RequestParam("address")String address,
                               @RequestParam("phoneNumber")String phoneNumber){
        Court court = new Court(name,type,city,address,phoneNumber);
        asyncCourtsService.addCourtAsync(court);
    }


    @PutMapping("/{oldId}")
    public void editCourtInRepo(@PathVariable("oldId") int oldId,
                                @RequestParam("name")String name,
                                @RequestParam("type")String type,
                                @RequestParam("city")String city,
                                @RequestParam("address")String address,
                                @RequestParam("phoneNumber")String phoneNumber) throws CourtNotFoundException {
        Court editedCourt = new Court(name,type,city,address,phoneNumber);
        asyncCourtsService.editCourtAsync(oldId,editedCourt);
    }
}
