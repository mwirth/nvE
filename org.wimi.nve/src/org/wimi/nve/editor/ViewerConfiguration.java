package org.wimi.nve.editor;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextHover;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.reconciler.DirtyRegion;
import org.eclipse.jface.text.reconciler.IReconciler;
import org.eclipse.jface.text.reconciler.IReconcilingStrategy;
import org.eclipse.jface.text.reconciler.MonoReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.editors.text.TextSourceViewerConfiguration;
import org.markdownj.MarkdownProcessor;
import org.wimi.nve.util.ColorStore;

public class ViewerConfiguration extends TextSourceViewerConfiguration
{
	private ColorStore colorTable;
	private Browser browser;

	public ViewerConfiguration(ColorStore colorTable, IPreferenceStore preferenceStore, Browser browser)
	{
		super(preferenceStore);
		this.colorTable = colorTable;
		this.browser = browser;
	}

	@Override
	public IPresentationReconciler getPresentationReconciler(ISourceViewer sourceViewer)
	{
		Scanner scanner = new Scanner(colorTable);

		PresentationReconciler pr = (PresentationReconciler) super.getPresentationReconciler(sourceViewer);
		DefaultDamagerRepairer ddr = new DefaultDamagerRepairer(scanner);

		pr.setRepairer(ddr, IDocument.DEFAULT_CONTENT_TYPE);
		pr.setDamager(ddr, IDocument.DEFAULT_CONTENT_TYPE);

		return pr;
	}

	@Override
	public IReconciler getReconciler(ISourceViewer sourceViewer)
	{
		IReconciler rs = super.getReconciler(sourceViewer);

		final IReconcilingStrategy superStrategy = rs == null ? null : rs.getReconcilingStrategy("text");

		IReconcilingStrategy strategy = new IReconcilingStrategy()
		{
			private IDocument document;

			public void reconcile(IRegion region)
			{
				// StringBuilder sb =
				// new StringBuilder(
				// "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01//EN\"><html><head><title>Title</title><link rel=\"stylesheet\" type=\"text/css\" href=\"github.css\"></head><body>");

				String cssPath = Platform.getInstanceLocation().getURL().getPath();
				System.out.println("cssPath:" + cssPath);

				StringBuilder sb =
					new StringBuilder(
						"<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01//EN\"><html><head><title>Title</title><link rel=\"stylesheet\" type=\"text/css\" href=\""
							+ cssPath + "github.css\"></head><body>");

				MarkdownProcessor markdown = new MarkdownProcessor();
				final String htmlText = markdown.markdown(document.get());
				System.out.println();
				System.out.println("---- html ----");
				System.out.println(htmlText);
				sb.append(htmlText).append("</body></html>");
				final String finalhtml = sb.toString();
				Display.getDefault().syncExec(new Runnable()
				{
					public void run()
					{
						browser.setText(finalhtml);
					}
				});
			}

			public void reconcile(DirtyRegion dirtyRegion, IRegion subRegion)
			{
				System.out.println("reconcile(DirtyRegion, IRegion)");
				System.out.println(dirtyRegion.getText());
				System.out.println(subRegion.getOffset());
				System.out.println(subRegion.getLength());
				MarkdownProcessor markdown = new MarkdownProcessor();

				System.out.println(document.get());
				String htmlText = markdown.markdown(document.get());
				System.out.println(htmlText);
				// update Browser
			}

			public void setDocument(IDocument document)
			{
				this.document = document;
				if (superStrategy != null)
					superStrategy.setDocument(document);
			}
		};

		MonoReconciler reconciler = new MonoReconciler(strategy, true);
		reconciler.setIsIncrementalReconciler(false);
		reconciler.setProgressMonitor(new NullProgressMonitor());
		reconciler.setDelay(100);

		return reconciler;
	}

	@Override
	public ITextHover getTextHover(ISourceViewer sourceViewer, String contentType)
	{
		return super.getTextHover(sourceViewer, contentType);
	}
}
