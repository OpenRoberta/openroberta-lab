package lejos.utility;

/** 
 * Implementation of a Kalman filter using the Matrix class
 */
public class KalmanFilter {
  private Matrix a, b, c, i, q, r, at, ct;
  private Matrix mu, sigma, muBar, sigmaBar, gain;
  
  public KalmanFilter(Matrix a, Matrix b, Matrix c, Matrix q, Matrix r) {
    this.a = a;
    this.b = b;
    this.c = c;
    this.q = q;
    this.r = r;
    this.at = a.transpose();
    this.ct = c.transpose();
  }
  
  public void setState(Matrix mean, Matrix covariance) {
    this.mu = mean;
    this.sigma = covariance;
    int n = mu.getRowDimension();
    this.i = Matrix.identity(n, n);
  }
  
  public void update(Matrix control, Matrix measurement) {
    // Control update step 1: calculate the predicted mean
	muBar = a.times(mu).plus(b.times(control));
    
    // Control update step 2: calculate the predicted covariance
    sigmaBar = a.times(sigma).times(at).plus(r);
   
    // Calculate the Kalman Gain 
    gain = sigmaBar.times(ct).times(c.times(sigmaBar).times(ct).plus(q).inverse());
    
    // Measurement update: calculate the new mean
    mu = muBar.plus(gain.times(measurement.minus(c.times(muBar))));
    
    // Calculate the new covariance
    sigma = i.minus(gain.times(c)).times(sigmaBar);
  }
  
  public Matrix getMean() {
    return mu;
  }
  
  public Matrix getCovariance() {
    return sigma;
  }
  
  public Matrix getPredictedMean() {
	  return muBar;
  }
  
  public Matrix getPredictedCovariance() {
	  return sigmaBar;
  }
  
  public Matrix getGain() {
	  return gain;
  }
}
