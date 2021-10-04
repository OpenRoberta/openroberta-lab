#include <webots/robot.h>
#include <webots/supervisor.h>
#include <webots/plugins/robot_window/default.h>
#include <stdio.h>
#include <string.h>

int main(int argc, char *argv[]) {
  wb_robot_init();
  
  const int time_step = wb_robot_get_basic_time_step();
  WbNodeRef nao = wb_supervisor_node_get_from_def("NAO");
  printf("nao = %p\n", nao);
  while(wb_robot_step(time_step) != -1) {
    const char *message = wb_robot_wwi_receive_text();
    if (message == NULL)
      continue;
    if (strncmp(message, "reset", 5) == 0) {
      wb_supervisor_simulation_reset();
    } else if (strncmp(message, "upload:", 7) == 0) {
      printf("received upload message\n");
      remove("../nao_demo_python/nao_demo_python.py"); // deletes default controller if any
      FILE *fd = fopen("../nao_demo_python/nao_demo_python.py", "w");
      fprintf(fd, "%s", &message[7]);
      fclose(fd);
      wb_supervisor_node_restart_controller(nao);
    }
  }
  return 1;
}