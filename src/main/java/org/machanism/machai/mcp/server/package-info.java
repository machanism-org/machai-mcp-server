/*-
 * @guidance:
 *
 * **IMPORTANT: UPDATE THIS `package-info.java`!**
 *
 * - Use Clear and Concise Descriptions:
 *     - Write meaningful summaries that explain the purpose, behavior, and usage of the package and its elements.
 *     - Avoid vague statements; be specific about functionality and intent.
 *
 * - Update `package-info.java`:
 *     - Generate comprehensive package-level Javadoc that clearly describes the package’s overall purpose, scope, and usage based on package-info.java files located on child folders.
 *     - Place the package-level Javadoc immediately before the `package` declaration.
 *
 *     - When generating Javadoc, if you encounter code blocks inside `<pre>` tags, escape `<` and `>` as `&lt;` and &gt; in `<pre>` content for Javadoc. 
 *     - Ensure that the code is properly escaped and formatted.
 *     
 * author: Viktor Tovstyi
 * since: 1.2.0
 */

/**
 * Provides the synchronous Model Context Protocol (MCP) server implementation
 * used to expose Machai function tools and prompts to MCP clients.
 *
 * <p>The package has three layers:</p>
 * <ul>
 * <li>{@link AbstractMcpServer} holds the common project-directory and
 * configuration behavior and defines the tool-registration and startup
 * lifecycle.</li>
 * <li>{@link GenericGenaiAdapter} and its prompt-aware subclass translate
 * Machai {@code ToolFunction} and {@code ParamDescriptor} definitions into MCP
 * tool schemas, invocation handlers, prompt definitions, and prompt results.
 * The stateless HTTP adapter also translates resource definitions into resource
 * read handlers.</li>
 * <li>{@link StdioMcpServer}, {@link HttpStatelessMcpServer}, and
 * {@link HttpStreamableMcpServer} supply STDIO, stateless HTTP, and streamable
 * HTTP transports respectively. {@link AbstractHttpMcpServer} centralizes the
 * Jetty connector, servlet, thread-pool, port, and startup setup shared by the
 * HTTP implementations.</li>
 * </ul>
 *
 * <h2>Tool and prompt execution</h2>
 * <p>
 * The server launcher discovers function-based tools through
 * {@link org.machanism.machai.process.tools.FunctionToolsLoader}. The optional
 * {@code enabledTools} configuration property limits registration; its values
 * may be separated by spaces, commas, semicolons, or other whitespace. Tool
 * parameters are represented as JSON Schema, required arguments are validated
 * by the MCP client against that schema, and function results are serialized
 * into MCP text content. Invocation failures are returned as error tool
 * results. Prompt arguments are exposed as MCP prompt arguments; string,
 * collection, and other serialized function results become prompt messages.
 * Streamable exchanges additionally make their session identifier available to
 * functions through the tool-function session parameter. Stateless exchanges
 * do not have a session identifier.
 * </p>
 *
 * <h2>Transport behavior</h2>
 * <ul>
 * <li>{@link StdioMcpServer} creates a single-session synchronous MCP server
 * over standard input and output, with tools, prompts, and logging enabled.</li>
 * <li>{@link HttpStatelessMcpServer} serves synchronous MCP requests through a
 * Jetty servlet without retaining server-side session state. It advertises
 * tools, prompts, and resources; resource functions receive the requested URI,
 * configured project directory, and configurator.</li>
 * <li>{@link HttpStreamableMcpServer} serves synchronous streamable HTTP
 * requests through Jetty and preserves the exchange session context for tool
 * and prompt calls.</li>
 * </ul>
 * <p>
 * HTTP startup errors are wrapped in {@link McpServerStartupException}.
 * Implementations report the Machai MCP homepage and icon metadata where the
 * transport supports server implementation metadata.
 * </p>
 *
 * <h2>Starting the server</h2>
 * <p>
 * {@link McpServer#main(String[])} is the command-line entry point. Use
 * {@code -n}/{@code --name} and {@code -v}/{@code --version} for server
 * metadata, {@code -d}/{@code --projectDir} for the project directory, and
 * {@code -c}/{@code --config} for an explicit properties file. Without an
 * explicit configuration path, {@link McpServer} attempts
 * {@link McpServer#MCP_CONFIG_FILE_NAME} ({@code mcp.properties}) and tolerates
 * that default file being absent. Without {@code -p}/{@code --port}, it starts
 * {@link StdioMcpServer}; with a port, it starts
 * {@link HttpStatelessMcpServer}, or {@link HttpStreamableMcpServer} when
 * {@code -s}/{@code --session} is also supplied.
 * </p>
 *
 * @author Viktor Tovstyi
 * @since 1.2.0
 */
package org.machanism.machai.mcp.server;
