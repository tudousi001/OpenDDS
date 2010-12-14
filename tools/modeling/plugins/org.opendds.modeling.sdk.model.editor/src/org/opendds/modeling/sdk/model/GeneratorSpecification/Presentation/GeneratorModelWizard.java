/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.opendds.modeling.sdk.model.GeneratorSpecification.Presentation;


import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.eclipse.emf.common.util.URI;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;

import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.emf.ecore.xmi.XMLResource;

import org.eclipse.emf.edit.ui.provider.ExtendedImageRegistry;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;

import org.eclipse.core.runtime.IProgressMonitor;

import org.eclipse.jface.dialogs.MessageDialog;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;

import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardPage;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

import org.eclipse.ui.actions.WorkspaceModifyOperation;

import org.eclipse.ui.dialogs.WizardNewFileCreationPage;

import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.ISetSelectionTarget;

import org.opendds.modeling.sdk.model.GeneratorSpecification.CodeGen;
import org.opendds.modeling.sdk.model.GeneratorSpecification.GeneratorFactory;
import org.opendds.modeling.sdk.model.GeneratorSpecification.GeneratorPackage;
import org.opendds.modeling.sdk.model.GeneratorSpecification.Generator.ParsedModelFile;
import org.opendds.modeling.sdk.model.GeneratorSpecification.Generator.SdkGeneratorFactory;
import org.opendds.modeling.sdk.model.GeneratorSpecification.Instance;
import org.opendds.modeling.sdk.model.GeneratorSpecification.Instances;
import org.opendds.modeling.sdk.model.GeneratorSpecification.ModelFile;
import org.opendds.modeling.sdk.model.GeneratorSpecification.TargetDir;
import org.opendds.modeling.sdk.model.GeneratorSpecification.Transport;
import org.opendds.modeling.sdk.model.GeneratorSpecification.TransportOffset;


import org.eclipse.core.runtime.Path;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;

import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;


/**
 * This is a simple wizard for creating a new model file.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class GeneratorModelWizard extends Wizard implements INewWizard {
	/**
	 * The supported extensions for created files.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final List<String> FILE_EXTENSIONS =
		Collections.unmodifiableList(Arrays.asList(GeneratorEditorPlugin.INSTANCE.getString("_UI_GeneratorEditorFilenameExtensions").split("\\s*,\\s*")));

	/**
	 * A formatted list of supported file extensions, suitable for display.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final String FORMATTED_FILE_EXTENSIONS =
		GeneratorEditorPlugin.INSTANCE.getString("_UI_GeneratorEditorFilenameExtensions").replaceAll("\\s*,\\s*", ", ");

	/**
	 * This caches an instance of the model package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected GeneratorPackage generatorPackage = GeneratorPackage.eINSTANCE;

	/**
	 * This caches an instance of the model factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected GeneratorFactory generatorFactory = generatorPackage.getGeneratorFactory();

	/**
	 * This is the file creation page.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected GeneratorModelWizardNewFileCreationPage newFileCreationPage;

	protected GeneratorModelWizardModelSelectionPage modelSelectionPage;

	/**
	 * Remember the selection during initialization for populating the default container.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected IStructuredSelection selection;

	/**
	 * Remember the workbench during initialization.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected IWorkbench workbench;
	
	protected URI modelFileURI;
	
	protected URI targetDirURI;
	
	/**
	 * This just records the information.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.workbench = workbench;
		this.selection = selection;
		setWindowTitle(GeneratorEditorPlugin.INSTANCE.getString("_UI_Wizard_label"));
		setDefaultPageImageDescriptor(ExtendedImageRegistry.INSTANCE.getImageDescriptor(GeneratorEditorPlugin.INSTANCE.getImage("full/wizban/NewGenerator")));
	}

	/**
	 * Create a new model.
	 * <!-- begin-user-doc -->
	 * These documents are always rooted with a "Code Gen" element.  The
	 * initial model will have the ModelFile, TargetDir, and Instances top
	 * level elements.  Instances will contain a single Instance with the
	 * name 'default', which will have a 'TransportOffset' and one
	 * 'Transport' element for each unique transport index used in the
	 * model.  All of these should be immutable, so that we can then
	 * validate that all other instances have the same transports defined.
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	protected EObject createInitialModel() {
		CodeGen codeGen = generatorFactory.createCodeGen();
		
		ModelFile modelFile = generatorFactory.createModelFile();
		if( modelFileURI != null && !modelFileURI.isEmpty()) {
			modelFile.setName( modelFileURI.toPlatformString(true));
		}

		TargetDir targetDir = generatorFactory.createTargetDir();
		if( targetDirURI != null && !targetDirURI.isEmpty()) {
			targetDir.setName( targetDirURI.toPlatformString(true));
		}
		codeGen.setTarget(targetDir);

		Instances instances = generatorFactory.createInstances();
		codeGen.setInstances(instances);
		
		Instance instance = generatorFactory.createInstance();
		
		TransportOffset transportOffset = generatorFactory.createTransportOffset();
		transportOffset.setValue(0);
		instance.setTransportOffset(transportOffset);

		if( modelFile.getName() != null) {
			// Load the default instance with a transport for each index found in the model.
			ParsedModelFile parsedModelFile = SdkGeneratorFactory.createParsedModelFile(
					(Window)this.workbench.getActiveWorkbenchWindow());
			Set<Integer> transportIndices = parsedModelFile.getTransportIds(modelFile.getName());
			for( Integer current : transportIndices) {
				Transport transport = generatorFactory.createTransport();
				transport.setTransportIndex(current);
				instance.getTransport().add(transport);
			}
			codeGen.setSource(modelFile);
		}

		instances.getInstance().add(instance);
		
		return codeGen;
	}

	/**
	 * Do the work after everything is specified.
	 * <!-- begin-user-doc -->
	 * Unconditionally use UTF-8 encoding.  This can be externalized later if it becomes necessary.
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public boolean performFinish() {
		try {
			// Remember the file.
			//
			final IFile modelFile = getModelFile();

			// Do the work within an operation.
			//
			WorkspaceModifyOperation operation =
				new WorkspaceModifyOperation() {
					@Override
					protected void execute(IProgressMonitor progressMonitor) {
						try {
							// Create a resource set
							//
							ResourceSet resourceSet = new ResourceSetImpl();

							// Get the URI of the model file.
							//
							URI fileURI = URI.createPlatformResourceURI(modelFile.getFullPath().toString(), true);

							// Create a resource for this file.
							//
							Resource resource = resourceSet.createResource(fileURI);

							// Add the initial model object to the contents.
							//
							EObject rootObject = createInitialModel();
							if (rootObject != null) {
								resource.getContents().add(rootObject);
							}

							// Save the contents of the resource to the file system.
							//
							Map<Object, Object> options = new HashMap<Object, Object>();
							options.put(XMLResource.OPTION_KEEP_DEFAULT_CONTENT, Boolean.TRUE);
							options.put(XMLResource.OPTION_ENCODING, "UTF-8");
							resource.save(options);
						}
						catch (Exception exception) {
							GeneratorEditorPlugin.INSTANCE.log(exception);
						}
						finally {
							progressMonitor.done();
						}
					}
				};

			getContainer().run(false, false, operation);

			// Select the new file resource in the current view.
			//
			IWorkbenchWindow workbenchWindow = workbench.getActiveWorkbenchWindow();
			IWorkbenchPage page = workbenchWindow.getActivePage();
			final IWorkbenchPart activePart = page.getActivePart();
			if (activePart instanceof ISetSelectionTarget) {
				final ISelection targetSelection = new StructuredSelection(modelFile);
				getShell().getDisplay().asyncExec
					(new Runnable() {
						 public void run() {
							 ((ISetSelectionTarget)activePart).selectReveal(targetSelection);
						 }
					 });
			}

			// Open an editor on the new file.
			//
			try {
				page.openEditor
					(new FileEditorInput(modelFile),
					 workbench.getEditorRegistry().getDefaultEditor(modelFile.getFullPath().toString()).getId());
			}
			catch (PartInitException exception) {
				MessageDialog.openError(workbenchWindow.getShell(), GeneratorEditorPlugin.INSTANCE.getString("_UI_OpenEditorError_label"), exception.getMessage());
				return false;
			}

			return true;
		}
		catch (Exception exception) {
			GeneratorEditorPlugin.INSTANCE.log(exception);
			return false;
		}
	}

	/**
	 * This is the one page of the wizard.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public class GeneratorModelWizardNewFileCreationPage extends WizardNewFileCreationPage {
		/**
		 * Pass in the selection.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		public GeneratorModelWizardNewFileCreationPage(String pageId, IStructuredSelection selection) {
			super(pageId, selection);
		}

		/**
		 * The framework calls this to see if the file is correct.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		@Override
		protected boolean validatePage() {
			if (super.validatePage()) {
				String extension = new Path(getFileName()).getFileExtension();
				if (extension == null || !FILE_EXTENSIONS.contains(extension)) {
					String key = FILE_EXTENSIONS.size() > 1 ? "_WARN_FilenameExtensions" : "_WARN_FilenameExtension";
					setErrorMessage(GeneratorEditorPlugin.INSTANCE.getString(key, new Object [] { FORMATTED_FILE_EXTENSIONS }));
					return false;
				}
				return true;
			}
			return false;
		}

		/**
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		public IFile getModelFile() {
			return ResourcesPlugin.getWorkspace().getRoot().getFile(getContainerFullPath().append(getFileName()));
		}
	}

	/**
	 * @author martinezm
	 *
	 */
	public class GeneratorModelWizardModelSelectionPage extends WizardPage {

		/**
		 * @param pageName
		 */
		public GeneratorModelWizardModelSelectionPage(String pageName) {
			super(pageName);
			// TODO Auto-generated constructor stub
		}

		/**
		 * @param pageName
		 * @param title
		 * @param titleImage
		 */
		public GeneratorModelWizardModelSelectionPage(String pageName,
				String title, ImageDescriptor titleImage) {
			super(pageName, title, titleImage);
			// TODO Auto-generated constructor stub
		}

		/* (non-Javadoc)
		 * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
		 */
		@Override
		public void createControl(Composite parent) {
			// TODO Auto-generated method stub

		}

	}

	/**
	 * The framework calls this to create the contents of the wizard.
	 * <!-- begin-user-doc -->
	 * We removed the root and encoding selections as they are hard coded for the editor.
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
		@Override
	public void addPages() {
		// Create a page, set the title, and the initial model file name.
		//
		newFileCreationPage = new GeneratorModelWizardNewFileCreationPage("Whatever", selection);
		newFileCreationPage.setTitle(GeneratorEditorPlugin.INSTANCE.getString("_UI_GeneratorModelWizard_label"));
		newFileCreationPage.setDescription(GeneratorEditorPlugin.INSTANCE.getString("_UI_GeneratorModelWizard_description"));
		newFileCreationPage.setFileName(GeneratorEditorPlugin.INSTANCE.getString("_UI_GeneratorEditorFilenameDefaultBase") + "." + FILE_EXTENSIONS.get(0));
		addPage(newFileCreationPage);

		// Try and get the resource selection to determine a current directory for the file dialog.
		//
		if (selection != null && !selection.isEmpty()) {
			// Get the resource...
			//
			Object selectedElement = selection.iterator().next();
			if (selectedElement instanceof IResource) {
				// Get the resource parent, if its a file.
				//
				IResource selectedResource = (IResource)selectedElement;
				if (selectedResource.getType() == IResource.FILE) {
					// If a file is selected, that will be used as the model
					// source file, and a new file will be created for this
					modelFileURI = URI.createPlatformResourceURI(selectedResource.getFullPath().toOSString(),true);
					selectedResource = selectedResource.getParent();
				}

				// This gives us a directory...
				//
				if (selectedResource instanceof IFolder || selectedResource instanceof IProject) {
					// Set this for the container.
					//
					newFileCreationPage.setContainerFullPath(selectedResource.getFullPath());

					// Make up a unique new name here.
					//
					String defaultGenBaseFilename = GeneratorEditorPlugin.INSTANCE.getString("_UI_GeneratorEditorFilenameDefaultBase");
					String defaultGenFilenameExtension = FILE_EXTENSIONS.get(0);
					String genFilename = defaultGenBaseFilename + "." + defaultGenFilenameExtension;
					for (int i = 1; ((IContainer)selectedResource).findMember(genFilename) != null; ++i) {
						genFilename = defaultGenBaseFilename + i + "." + defaultGenFilenameExtension;
					}
					newFileCreationPage.setFileName(genFilename);
				}
			}
		}
		
//		modelSelectionPage = new GeneratorModelWizardModelSelectionPage("Model File and Target","Model File and Target",null);
//		addPage(modelSelectionPage);
	}

	/**
	 * Get the file from the page.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IFile getModelFile() {
		return newFileCreationPage.getModelFile();
	}

}
