# Template principal
resource "aws_launch_template" "main_lt" {
  name_prefix   = "systemParking-"
  image_id      = data.aws_ami.amazon_linux.id
  instance_type = var.instance_type

  vpc_security_group_ids = [aws_security_group.instance_sg.id]

  user_data = base64encode(templatefile("${path.module}/user_data.sh.tpl", {
    docker_image = var.docker_image
  }))
}

# Entry Service
resource "aws_launch_template" "entry_lt" {
  name_prefix   = "EntryService-"
  image_id      = data.aws_ami.amazon_linux.id
  instance_type = var.instance_type

  vpc_security_group_ids = [aws_security_group.instance_sg.id]

  user_data = base64encode(templatefile("${path.module}/user_data_entry.sh.tpl", {
    docker_image = var.entry_service_image
  }))
}

# Exit Service
resource "aws_launch_template" "exit_lt" {
  name_prefix   = "ExitService-"
  image_id      = data.aws_ami.amazon_linux.id
  instance_type = var.instance_type

  vpc_security_group_ids = [aws_security_group.instance_sg.id]

  user_data = base64encode(templatefile("${path.module}/user_data_exit.sh.tpl", {
    docker_image = var.exit_service_image
  }))
}

# Parking Space Service
resource "aws_launch_template" "parking_space_lt" {
  name_prefix   = "ParkingSpaceService-"
  image_id      = data.aws_ami.amazon_linux.id
  instance_type = var.instance_type

  vpc_security_group_ids = [aws_security_group.instance_sg.id]

  user_data = base64encode(templatefile("${path.module}/user_data_parking_space.sh.tpl", {
    docker_image = var.parking_space_service_image
  }))
}
