package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;


@TeleOp(name="Boilbot_Teleop")
@Config
public class Boilbot_Teleop extends LinearOpMode {
    private DcMotor leftBack;
    private DcMotor rightBack;
    private DcMotor leftFront;
    private DcMotor rightFront;
    private Servo claw;
    private DcMotorEx arm;


    public static double claw_open = 0.35;
    public static double claw_closed = 0.83;
    public static double arm_up = 0.83;
    public static double arm_down = 0.83;



    @Override
    public void runOpMode() {

        double armHeight = arm_down;
        double claw_position = claw_open;


        leftBack = hardwareMap.get(DcMotor.class, "leftBackMotor");
        rightBack = hardwareMap.get(DcMotor.class, "rightBackMotor");
        leftFront = hardwareMap.get(DcMotor.class, "leftFrontMotor");
        rightFront = hardwareMap.get(DcMotor.class, "rightFrontMotor");

        rightFront.setDirection(DcMotorSimple.Direction.REVERSE);
        rightBack.setDirection(DcMotorSimple.Direction.REVERSE);

        //set the motors to "coast mode" when stopped to prevent tipping
        leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        leftBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        rightBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        arm = hardwareMap.get(DcMotorEx.class,"arm");
        claw = hardwareMap.get(Servo.class, "claw");

        arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);


        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive()) {

            /****************************************************************
             Drive code -- basic mecanum drive
             ****************************************************************/

            double y = -gamepad1.left_stick_y; // Remember, Y stick value is reversed
            double x = gamepad1.left_stick_x * 1.1; // Counteract imperfect strafing
            double rx = gamepad1.right_stick_x;

            double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
            double frontLeftPower = (y + x + rx) / denominator;
            double backLeftPower = (y - x + rx) / denominator;
            double frontRightPower = (y - x - rx) / denominator;
            double backRightPower = (y + x - rx) / denominator;


            leftFront.setPower(frontLeftPower);
            leftBack.setPower(backLeftPower);
            rightFront.setPower(frontRightPower);
            rightBack.setPower(backRightPower);


            /****************************************************************
             Intake code -- the intake has two components: arm and claw
             Arm has two positions: arm_down, arm_up
             Claw has two positions: open and closed
             ****************************************************************/

            if(gamepad1.y){
                claw_position = claw_open;
            }

            if (gamepad1.x){
                claw_position = claw_closed;
            }

            if (gamepad1.a) {
                armHeight = arm_down;
            }

            if (gamepad1.b) {
                armHeight = arm_up;
            }



            /***************************************************************
             Now take all the calculated variables and send the right values
             to the right motors
             ***************************************************************/

           arm.setPower(armHeight);
           claw.setPosition(claw_position);


           telemetry.update();

        }
    }
}
