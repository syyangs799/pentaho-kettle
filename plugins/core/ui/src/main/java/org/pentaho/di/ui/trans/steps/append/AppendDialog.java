/*! ******************************************************************************
 *
 * Pentaho
 *
 * Copyright (C) 2024 by Hitachi Vantara, LLC : http://www.pentaho.com
 *
 * Use of this software is governed by the Business Source License included
 * in the LICENSE.TXT file.
 *
 * Change Date: 2029-07-20
 ******************************************************************************/


package org.pentaho.di.ui.trans.steps.append;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.pentaho.di.core.Const;
import org.pentaho.di.core.annotations.PluginDialog;
import org.pentaho.di.core.util.Utils;
import org.pentaho.di.i18n.BaseMessages;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.BaseStepMeta;
import org.pentaho.di.trans.step.StepDialogInterface;
import org.pentaho.di.trans.step.errorhandling.StreamInterface;
import org.pentaho.di.trans.steps.append.AppendMeta;
import org.pentaho.di.ui.trans.step.BaseStepDialog;

/**
 * Dialog for the append step.
 *
 * @author Sven Boden
 */
@PluginDialog( id = "Append", image = "APP.svg", pluginType = PluginDialog.PluginType.STEP,
  documentationUrl = "http://wiki.pentaho.com/display/EAI/Append+streams" )
public class AppendDialog extends BaseStepDialog implements StepDialogInterface {
  private static Class<?> PKG = AppendDialog.class; // for i18n purposes, needed by Translator2!!

  private Label wlHeadHop;
  private CCombo wHeadHop;
  private FormData fdlHeadHop, fdHeadHop;

  private Label wlTailHop;
  private CCombo wTailHop;
  private FormData fdlTailHop, fdTailHop;

  private AppendMeta input;

  public AppendDialog( Shell parent, Object in, TransMeta tr, String sname ) {
    super( parent, (BaseStepMeta) in, tr, sname );
    input = (AppendMeta) in;
  }

  public String open() {
    Shell parent = getParent();
    Display display = parent.getDisplay();

    shell = new Shell( parent, SWT.DIALOG_TRIM | SWT.RESIZE | SWT.MIN | SWT.MAX );
    props.setLook( shell );
    setShellImage( shell, input );

    ModifyListener lsMod = new ModifyListener() {
      public void modifyText( ModifyEvent e ) {
        input.setChanged();
      }
    };
    backupChanged = input.hasChanged();

    FormLayout formLayout = new FormLayout();
    formLayout.marginWidth = Const.FORM_MARGIN;
    formLayout.marginHeight = Const.FORM_MARGIN;

    shell.setLayout( formLayout );
    shell.setText( BaseMessages.getString( PKG, "AppendDialog.Shell.Label" ) );

    int middle = props.getMiddlePct();
    int margin = Const.MARGIN;

    // Stepname line
    wlStepname = new Label( shell, SWT.RIGHT );
    wlStepname.setText( BaseMessages.getString( PKG, "AppendDialog.Stepname.Label" ) );
    props.setLook( wlStepname );
    fdlStepname = new FormData();
    fdlStepname.left = new FormAttachment( 0, 0 );
    fdlStepname.right = new FormAttachment( middle, -margin );
    fdlStepname.top = new FormAttachment( 0, margin );
    wlStepname.setLayoutData( fdlStepname );
    wStepname = new Text( shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER );
    wStepname.setText( stepname );
    props.setLook( wStepname );
    wStepname.addModifyListener( lsMod );
    fdStepname = new FormData();
    fdStepname.left = new FormAttachment( middle, 0 );
    fdStepname.top = new FormAttachment( 0, margin );
    fdStepname.right = new FormAttachment( 100, 0 );
    wStepname.setLayoutData( fdStepname );

    // Get the previous steps...
    String[] previousSteps = transMeta.getPrevStepNames( stepname );

    wlHeadHop = new Label( shell, SWT.RIGHT );
    wlHeadHop.setText( BaseMessages.getString( PKG, "AppendDialog.HeadHop.Label" ) );
    props.setLook( wlHeadHop );
    fdlHeadHop = new FormData();
    fdlHeadHop.left = new FormAttachment( 0, 0 );
    fdlHeadHop.right = new FormAttachment( middle, -margin );
    fdlHeadHop.top = new FormAttachment( wStepname, margin );
    wlHeadHop.setLayoutData( fdlHeadHop );
    wHeadHop = new CCombo( shell, SWT.BORDER );
    props.setLook( wHeadHop );

    if ( previousSteps != null ) {
      wHeadHop.setItems( previousSteps );
    }

    wHeadHop.addModifyListener( lsMod );
    fdHeadHop = new FormData();
    fdHeadHop.left = new FormAttachment( middle, 0 );
    fdHeadHop.top = new FormAttachment( wStepname, margin );
    fdHeadHop.right = new FormAttachment( 100, 0 );
    wHeadHop.setLayoutData( fdHeadHop );

    wlTailHop = new Label( shell, SWT.RIGHT );
    wlTailHop.setText( BaseMessages.getString( PKG, "AppendDialog.TailHop.Label" ) );
    props.setLook( wlTailHop );
    fdlTailHop = new FormData();
    fdlTailHop.left = new FormAttachment( 0, 0 );
    fdlTailHop.right = new FormAttachment( middle, -margin );
    fdlTailHop.top = new FormAttachment( wHeadHop, margin );
    wlTailHop.setLayoutData( fdlTailHop );
    wTailHop = new CCombo( shell, SWT.BORDER );
    props.setLook( wTailHop );

    if ( previousSteps != null ) {
      wTailHop.setItems( previousSteps );
    }

    wTailHop.addModifyListener( lsMod );
    fdTailHop = new FormData();
    fdTailHop.top = new FormAttachment( wHeadHop, margin );
    fdTailHop.left = new FormAttachment( middle, 0 );
    fdTailHop.right = new FormAttachment( 100, 0 );
    wTailHop.setLayoutData( fdTailHop );

    // Some buttons
    wOK = new Button( shell, SWT.PUSH );
    wOK.setText( BaseMessages.getString( PKG, "System.Button.OK" ) );
    wCancel = new Button( shell, SWT.PUSH );
    wCancel.setText( BaseMessages.getString( PKG, "System.Button.Cancel" ) );

    setButtonPositions( new Button[] { wOK, wCancel }, margin, wTailHop );

    // Add listeners
    lsCancel = new Listener() {
      public void handleEvent( Event e ) {
        cancel();
      }
    };
    lsOK = new Listener() {
      public void handleEvent( Event e ) {
        ok();
      }
    };

    wCancel.addListener( SWT.Selection, lsCancel );
    wOK.addListener( SWT.Selection, lsOK );

    lsDef = new SelectionAdapter() {
      public void widgetDefaultSelected( SelectionEvent e ) {
        ok();
      }
    };

    wStepname.addSelectionListener( lsDef );

    // Detect X or ALT-F4 or something that kills this window...
    shell.addShellListener( new ShellAdapter() {
      public void shellClosed( ShellEvent e ) {
        cancel();
      }
    } );

    // Set the shell size, based upon previous time...
    setSize();

    getData();
    input.setChanged( backupChanged );

    shell.open();
    while ( !shell.isDisposed() ) {
      if ( !display.readAndDispatch() ) {
        display.sleep();
      }
    }
    return stepname;
  }

  /**
   * Copy information from the meta-data input to the dialog fields.
   */
  public void getData() {
    List<StreamInterface> infoStreams = input.getStepIOMeta().getInfoStreams();
    StreamInterface headStream = infoStreams.get( 0 );
    StreamInterface tailStream = infoStreams.get( 1 );

    wHeadHop.setText( Const.NVL( headStream.getStepname(), "" ) );
    wTailHop.setText( Const.NVL( tailStream.getStepname(), "" ) );

    wStepname.selectAll();
    wStepname.setFocus();
  }

  private void cancel() {
    stepname = null;
    input.setChanged( backupChanged );
    dispose();
  }

  private void ok() {
    if ( Utils.isEmpty( wStepname.getText() ) ) {
      return;
    }

    List<StreamInterface> infoStreams = input.getStepIOMeta().getInfoStreams();
    StreamInterface headStream = infoStreams.get( 0 );
    StreamInterface tailStream = infoStreams.get( 1 );

    headStream.setStepMeta( transMeta.findStep( wHeadHop.getText() ) );
    tailStream.setStepMeta( transMeta.findStep( wTailHop.getText() ) );

    stepname = wStepname.getText(); // return value

    dispose();
  }
}
