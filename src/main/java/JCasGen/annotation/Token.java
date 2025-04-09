

   
/* Apache UIMA v3 - First created by JCasGen Sun Mar 23 21:13:35 CET 2025 */

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
 * Updated by JCasGen Sun Mar 23 21:13:35 CET 2025
 * XML source: C:/Users/voron/Desktop/PPR/projects/multimodal_parliament_explorer_11_3/src/main/resources/typesystems/token.xml
 * @generated */
public class Token extends Annotation {
 
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static String _TypeName = "annotation.Token";
  
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(Token.class);
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
  public final static String _FeatName_Parent = "Parent";
  public final static String _FeatName_Lemma = "Lemma";
  public final static String _FeatName_PosTag = "PosTag";


  /* Feature Adjusted Offsets */
  private final static CallSite _FC_Id = TypeSystemImpl.createCallSite(Token.class, "Id");
  private final static MethodHandle _FH_Id = _FC_Id.dynamicInvoker();
  private final static CallSite _FC_Parent = TypeSystemImpl.createCallSite(Token.class, "Parent");
  private final static MethodHandle _FH_Parent = _FC_Parent.dynamicInvoker();
  private final static CallSite _FC_Lemma = TypeSystemImpl.createCallSite(Token.class, "Lemma");
  private final static MethodHandle _FH_Lemma = _FC_Lemma.dynamicInvoker();
  private final static CallSite _FC_PosTag = TypeSystemImpl.createCallSite(Token.class, "PosTag");
  private final static MethodHandle _FH_PosTag = _FC_PosTag.dynamicInvoker();

   
  /** Never called.  Disable default constructor
   * @generated */
  @Deprecated
  @SuppressWarnings ("deprecation")
  protected Token() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated
   * @param casImpl the CAS this Feature Structure belongs to
   * @param type the type of this Feature Structure 
   */
  public Token(TypeImpl type, CASImpl casImpl) {
    super(type, casImpl);
    readObject();
  }
  
  /** @generated
   * @param jcas JCas to which this Feature Structure belongs 
   */
  public Token(JCas jcas) {
    super(jcas);
    readObject();   
  } 


  /** @generated
   * @param jcas JCas to which this Feature Structure belongs
   * @param begin offset to the begin spot in the SofA
   * @param end offset to the end spot in the SofA 
  */  
  public Token(JCas jcas, int begin, int end) {
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
  //* Feature: Parent

  /** getter for Parent - gets 
   * @generated
   * @return value of the feature 
   */
  public String getParent() { 
    return _getStringValueNc(wrapGetIntCatchException(_FH_Parent));
  }
    
  /** setter for Parent - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setParent(String v) {
    _setStringValueNfc(wrapGetIntCatchException(_FH_Parent), v);
  }    
    
   
    
  //*--------------*
  //* Feature: Lemma

  /** getter for Lemma - gets 
   * @generated
   * @return value of the feature 
   */
  public String getLemma() { 
    return _getStringValueNc(wrapGetIntCatchException(_FH_Lemma));
  }
    
  /** setter for Lemma - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setLemma(String v) {
    _setStringValueNfc(wrapGetIntCatchException(_FH_Lemma), v);
  }    
    
   
    
  //*--------------*
  //* Feature: PosTag

  /** getter for PosTag - gets 
   * @generated
   * @return value of the feature 
   */
  public String getPosTag() { 
    return _getStringValueNc(wrapGetIntCatchException(_FH_PosTag));
  }
    
  /** setter for PosTag - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setPosTag(String v) {
    _setStringValueNfc(wrapGetIntCatchException(_FH_PosTag), v);
  }    
    
  }

    