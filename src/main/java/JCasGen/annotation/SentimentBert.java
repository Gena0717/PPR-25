

   
/* Apache UIMA v3 - First created by JCasGen Sun Mar 23 21:12:17 CET 2025 */

package annotation;
 

import java.lang.invoke.CallSite;
import java.lang.invoke.MethodHandle;

import org.apache.uima.cas.impl.CASImpl;
import org.apache.uima.cas.impl.TypeImpl;
import org.apache.uima.cas.impl.TypeSystemImpl;
import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;


import org.apache.uima.jcas.tcas.Annotation;


/** 
 * Updated by JCasGen Sun Mar 23 21:12:17 CET 2025
 * XML source: C:/Users/voron/Desktop/PPR/projects/multimodal_parliament_explorer_11_3/src/main/resources/typesystems/sentiment.xml
 * @generated */
public class SentimentBert extends Annotation {
 
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static String _TypeName = "annotation.SentimentBert";
  
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(SentimentBert.class);
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int type = typeIndexID;
  /** @generated
   * @return index of the type  
   */
  @Override
  public              int getTypeIndexID() {return typeIndexID;}
 
 
  /* *******************
   *   Feature Offsets *
   * *******************/ 
   
  public final static String _FeatName_Id = "Id";
  public final static String _FeatName_Sentiment = "Sentiment";
  public final static String _FeatName_Positiv = "Positiv";
  public final static String _FeatName_Neutral = "Neutral";
  public final static String _FeatName_Negativ = "Negativ";


  /* Feature Adjusted Offsets */
  private final static CallSite _FC_Id = TypeSystemImpl.createCallSite(SentimentBert.class, "Id");
  private final static MethodHandle _FH_Id = _FC_Id.dynamicInvoker();
  private final static CallSite _FC_Sentiment = TypeSystemImpl.createCallSite(SentimentBert.class, "Sentiment");
  private final static MethodHandle _FH_Sentiment = _FC_Sentiment.dynamicInvoker();
  private final static CallSite _FC_Positiv = TypeSystemImpl.createCallSite(SentimentBert.class, "Positiv");
  private final static MethodHandle _FH_Positiv = _FC_Positiv.dynamicInvoker();
  private final static CallSite _FC_Neutral = TypeSystemImpl.createCallSite(SentimentBert.class, "Neutral");
  private final static MethodHandle _FH_Neutral = _FC_Neutral.dynamicInvoker();
  private final static CallSite _FC_Negativ = TypeSystemImpl.createCallSite(SentimentBert.class, "Negativ");
  private final static MethodHandle _FH_Negativ = _FC_Negativ.dynamicInvoker();

   
  /** Never called.  Disable default constructor
   * @generated */
  @Deprecated
  @SuppressWarnings ("deprecation")
  protected SentimentBert() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated
   * @param casImpl the CAS this Feature Structure belongs to
   * @param type the type of this Feature Structure 
   */
  public SentimentBert(TypeImpl type, CASImpl casImpl) {
    super(type, casImpl);
    readObject();
  }
  
  /** @generated
   * @param jcas JCas to which this Feature Structure belongs 
   */
  public SentimentBert(JCas jcas) {
    super(jcas);
    readObject();   
  } 


  /** @generated
   * @param jcas JCas to which this Feature Structure belongs
   * @param begin offset to the begin spot in the SofA
   * @param end offset to the end spot in the SofA 
  */  
  public SentimentBert(JCas jcas, int begin, int end) {
    super(jcas);
    setBegin(begin);
    setEnd(end);
    readObject();
  }   

  /** 
   * <!-- begin-user-doc -->
   * Write your own initialization here
   * <!-- end-user-doc -->
   *
   * @generated modifiable 
   */
  private void readObject() {/*default - does nothing empty block */}
     
 
    
  //*--------------*
  //* Feature: Id

  /** getter for Id - gets 
   * @generated
   * @return value of the feature 
   */
  public String getId() { 
    return _getStringValueNc(wrapGetIntCatchException(_FH_Id));
  }
    
  /** setter for Id - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setId(String v) {
    _setStringValueNfc(wrapGetIntCatchException(_FH_Id), v);
  }    
    
   
    
  //*--------------*
  //* Feature: Sentiment

  /** getter for Sentiment - gets 
   * @generated
   * @return value of the feature 
   */
  public double getSentiment() { 
    return _getDoubleValueNc(wrapGetIntCatchException(_FH_Sentiment));
  }
    
  /** setter for Sentiment - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setSentiment(double v) {
    _setDoubleValueNfc(wrapGetIntCatchException(_FH_Sentiment), v);
  }    
    
   
    
  //*--------------*
  //* Feature: Positiv

  /** getter for Positiv - gets 
   * @generated
   * @return value of the feature 
   */
  public double getPositiv() { 
    return _getDoubleValueNc(wrapGetIntCatchException(_FH_Positiv));
  }
    
  /** setter for Positiv - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setPositiv(double v) {
    _setDoubleValueNfc(wrapGetIntCatchException(_FH_Positiv), v);
  }    
    
   
    
  //*--------------*
  //* Feature: Neutral

  /** getter for Neutral - gets 
   * @generated
   * @return value of the feature 
   */
  public double getNeutral() { 
    return _getDoubleValueNc(wrapGetIntCatchException(_FH_Neutral));
  }
    
  /** setter for Neutral - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setNeutral(double v) {
    _setDoubleValueNfc(wrapGetIntCatchException(_FH_Neutral), v);
  }    
    
   
    
  //*--------------*
  //* Feature: Negativ

  /** getter for Negativ - gets 
   * @generated
   * @return value of the feature 
   */
  public double getNegativ() { 
    return _getDoubleValueNc(wrapGetIntCatchException(_FH_Negativ));
  }
    
  /** setter for Negativ - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setNegativ(double v) {
    _setDoubleValueNfc(wrapGetIntCatchException(_FH_Negativ), v);
  }    
    
  }

    