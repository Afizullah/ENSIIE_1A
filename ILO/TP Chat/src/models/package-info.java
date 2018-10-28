package models;

/**
 * Sub-package containing data models classes such as
 * <ul>
 * <li>{@link models.Message} a class containing the messages broadcasted by the
 * server to all users</li>
 * <li>{@link models.NameSetListModel} a class containing the names of all users
 * that have sent a message (eventually sorted using {@link models.Message}s
 * ordering criteria. Such a model can be used in a {@link javax.swing.JList}
 * for instance.</li>
 * <li>{@link models.AuthorListFilter} a class implementing a
 * {@link java.util.function.Predicate} on the {@link models.Message}s and used
 * to filter Message streams with authors registered in the filter</li>
 * <li>{@link models.OSCheck} a class to check the nature of the Operating
 * System</li>
 * </ul>
 */
