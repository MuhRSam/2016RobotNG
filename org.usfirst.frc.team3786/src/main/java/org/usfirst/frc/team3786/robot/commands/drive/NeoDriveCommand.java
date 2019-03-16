package org.usfirst.frc.team3786.robot.commands.drive;

import org.usfirst.frc.team3786.robot.utils.Gyroscope;
import org.usfirst.frc.team3786.robot.utils.BNO055.CalData;
import org.usfirst.frc.team3786.robot.config.ui.UIConfig;
import org.usfirst.frc.team3786.robot.subsystems.DriveTrain;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class NeoDriveCommand extends Command {

	public static NeoDriveCommand instance;

	private boolean isGyroCalibrated = true;
	double targetHeading = 0.0;

	public static NeoDriveCommand getInstance() {
		if (instance == null)
			instance = new NeoDriveCommand();
		return instance;
	}

	public NeoDriveCommand() {
		// Use requires() here to declare subsystem dependencies
		requires(DriveTrain.getInstance());
	}

	// Called just before this Command runs the first time
	protected void initialize() {
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
		if (!isGyroCalibrated) {
			CalData calibration = Gyroscope.getInstance().getCalibration();
			if (calibration.accel > 1 && calibration.gyro > 2 && calibration.mag > 2 && calibration.sys > 1) {
				isGyroCalibrated = true;
			}
		}
		// When the number is negative, the wheels go forwards.
        // When the number is positive, the wheels go backwards.
        
		double throttle = UIConfig.getInstance().getLeftStickY();
		double turn = UIConfig.getInstance().getRightStickX();
		boolean useTargetHeading = false;
		// driver wants to go straight, haven't started using currentHeading yet.
		if (turn == 0 && throttle != 0 && isGyroCalibrated) {
			targetHeading = Gyroscope.getInstance().getHeadingContinuous();
			useTargetHeading = true;
		}
		// going straight with gyro
		if (useTargetHeading) {
			DriveTrain.getInstance().gyroStraight(-throttle, targetHeading);
			SmartDashboard.putBoolean("Straight with Gyro?", true);
		}
		// driving with turn
		else {
			DriveTrain.getInstance().arcadeDrive(-throttle, turn);
			SmartDashboard.putBoolean("Straight with Gyro?", false);
		}
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		return false;
	}

	// Called once after isFinished returns true
	protected void end() {
	}
}
