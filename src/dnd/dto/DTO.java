package dnd.dto;

import dnd.cli.view.View;

/**
 * Allows using the visitor pattern to resolve the specific type the view needs
 * in order to print different information based on the DTO's type.
 */
public interface DTO {
    String accept(View view);
}
