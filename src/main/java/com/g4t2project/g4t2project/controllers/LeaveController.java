@RestController
@RequestMapping("/leave")
public class LeaveController {
    @Autowired
    private LeaveApplicationService leaveApplicationService;

    @PostMapping("/apply")
    public ResponseEntity<String> applyForLeave(@RequestBody LeaveApplicationDto leaveApplicationDto) {
        leaveApplicationService.applyForLeave(leaveApplicationDto);
        return ResponseEntity.ok("Leave application submitted successfully");
    }

    @PostMapping("/upload-mc")
    public ResponseEntity<String> uploadMcDocument(@RequestParam("leaveId") int leaveId, @RequestParam("mcDocument") MultipartFile mcDocument) {
        leaveApplicationService.uploadMcDocument(leaveId, mcDocument);
        return ResponseEntity.ok("MC document uploaded successfully");
    }

    @PostMapping("/approve")
    public ResponseEntity<String> approveLeave(@RequestBody int leaveId) {
        leaveApplicationService.approveLeave(leaveId);
        return ResponseEntity.ok("Leave approved");
    }
}
