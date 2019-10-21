/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */

public class Robot extends IterativeRobot {
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();

  //Motor Controllers
  public WPI_TalonSRX leftFront = new WPI_TalonSRX(2);
  public WPI_TalonSRX leftBack = new WPI_TalonSRX(1);
  public WPI_TalonSRX rightFront = new WPI_TalonSRX(3);
  public WPI_TalonSRX rightBack = new WPI_TalonSRX(4);
  
  public SpeedControllerGroup left = new SpeedControllerGroup(leftFront, leftBack);
  public SpeedControllerGroup right = new SpeedControllerGroup(rightFront, rightBack);
  
  public DifferentialDrive tankDrive = new DifferentialDrive(left, right);
  public Joystick driver = new Joystick(0);
  public Joystick operator = new Joystick(1);
  boolean arcade;
  boolean target=false;
  double drive = 0.0;
  double steer = 0.0;
  double tv;
  double tx;
  double ty;
  double ta;
  
  /**
   * This function is run when the robot is first started up and should be
   * used for any initialization code.
   */

  public void drive(double leftSpeed, double rightSpeed){
    double zRotation = arcCos(leftSpeed,rightSpeed);
    double speed = 0.8;
    right.setInverted(true);
    if (!arcade){
      tankDrive.tankDrive(speed*leftSpeed, speed*rightSpeed);
    }else{
      tankDrive.arcadeDrive(speed*Math.sqrt(leftSpeed*leftSpeed+rightSpeed*rightSpeed), zRotation);
      System.out.print(""+arcCos(leftSpeed, rightSpeed)+"");
    }
  }
  public double arcCos(double x, double y){
    double theta=0;
    if(y!=0){
      theta=Math.atan(-x/y);
    }
    if (y>0){
      theta+=Math.PI;
    }
    
    return theta;
  }
   public boolean getButtonStart(Joystick joy){
     return joy.getRawButton(10);
  }
  public void LL(){
    
  }

   @Override
  public void robotInit() {
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);
  }

  /**
   * This function is called every robot packet, no matter the mode. Use
   * this for items like diagnostics that you want ran during disabled,
   * autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before
   * LiveWindow and SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
  }

  /**
   * This autonomous (along with the chooser code above) shows how to select
   * between different autonomous modes using the dashboard. The sendable
   * chooser code works with the Java SmartDashboard. If you prefer the
   * LabVIEW Dashboard, remove all of the chooser code and uncomment the
   * getString line to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional comparisons to
   * the switch structure below with additional strings. If using the
   * SendableChooser make sure to add them to the chooser code above as well.
   */
  @Override
  public void autonomousInit() {
    m_autoSelected = m_chooser.getSelected();
    // autoSelected = SmartDashboard.getString("Auto Selector",
    // defaultAuto);
    System.out.println("Auto selected: " + m_autoSelected);
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
    
    switch (m_autoSelected) {
      case kCustomAuto:
        // Put custom auto code here
        break;
      case kDefaultAuto:
      default:
        // Put default auto code here
        break;
    }
  }

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {
    arcade=getButtonStart(driver);
    if (!arcade){
      drive(driver.getRawAxis(1), driver.getRawAxis(3));
    }else {
      drive(driver.getRawAxis(0), driver.getRawAxis(1));
    }
  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
  }
}
