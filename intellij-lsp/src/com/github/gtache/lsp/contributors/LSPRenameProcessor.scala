package com.github.gtache.lsp.contributors

import java.util

import com.github.gtache.lsp.contributors.psi.LSPPsiElement
import com.github.gtache.lsp.editor.EditorEventManager
import com.github.gtache.lsp.requests.WorkspaceEditHandler
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.fileEditor.{FileEditorManager, TextEditor}
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.{PsiElement, PsiFile, PsiReference}
import com.intellij.refactoring.listeners.RefactoringElementListener
import com.intellij.refactoring.rename.{RenameDialog, RenamePsiElementProcessor}
import com.intellij.usageView.UsageInfo

import scala.collection.mutable

class LSPRenameProcessor extends RenamePsiElementProcessor {

  private var curElem: PsiElement = _
  private var elements: mutable.Set[PsiElement] = mutable.Set()
  private var openedEditors: mutable.Set[VirtualFile] = mutable.Set()

  override def canProcessElement(element: PsiElement): Boolean = {
    element match {
      case lsp: LSPPsiElement => true
      case file: PsiFile =>
        val editorO = FileEditorManager.getInstance(file.getProject).getAllEditors(file.getVirtualFile).collect { case t: TextEditor => t.getEditor }.headOption
        val manager = editorO.collect { case e: Editor if EditorEventManager.forEditor(e).nonEmpty => EditorEventManager.forEditor(e).get }
        manager match {
          case Some(m) =>
            val editor = editorO.get
            if (editor.getContentComponent.hasFocus) {
              val offset = editor.getCaretModel.getCurrentCaret.getOffset
              val (elements, openedEditors) = m.references(offset, getOriginalElement = true)
              this.elements ++= elements.toSet
              this.openedEditors ++= openedEditors.toSet
              this.curElem = elements.find(e => {
                val range = e.getTextRange
                val start = range.getStartOffset
                val end = range.getEndOffset
                start <= offset && offset <= end
              }).orNull
              true
            } else false
          case None => false
        }
      case _ => false
    }
  }

  /*  override def prepareRenaming(element: PsiElement, newName: String, allRenames: util.Map[PsiElement, String]): Unit = {
      prepareRenaming(element, newName, allRenames, element.getUseScope)
    }

    override def prepareRenaming(element: PsiElement, newName: String, allRenames: util.Map[PsiElement, String], scope: SearchScope): Unit = {
      elements.foreach(elem => allRenames.put(elem, newName))
    }*/

  override def createRenameDialog(project: Project, element: PsiElement, nameSuggestionContext: PsiElement, editor: Editor): RenameDialog =
    super.createRenameDialog(project, curElem, nameSuggestionContext, editor)

  override def findReferences(element: PsiElement): util.Collection[PsiReference] = {
    findReferences(element, searchInCommentsAndStrings = false)
  }

  override def findReferences(element: PsiElement, searchInCommentsAndStrings: Boolean): util.Collection[PsiReference] = {
    import scala.collection.JavaConverters._
    element match {
      case lsp: LSPPsiElement => if (elements.contains(lsp)) elements.map(e => e.getReference).asJava else Seq().asJava
      case _ => Seq().asJava
    }
  }

  override def isInplaceRenameSupported: Boolean = true

  override def renameElement(element: PsiElement, newName: String, usages: Array[UsageInfo], listener: RefactoringElementListener): Unit = {
    WorkspaceEditHandler.applyEdit(element, newName, usages, listener, openedEditors.clone())
    openedEditors.clear()
    elements.clear()
    curElem = null
  }
}
