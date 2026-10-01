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
 * Provides the synchronous Model Context Protocol (MCP) server runtime for
 * exposing Machai function tools, prompts, and, for stateless HTTP, resources.
 * The package contains the command-line launcher, transport-specific servers,
 * and adapters that translate Machai function definitions into MCP schemas and
 * request handlers.
 *
 * <h2>Common server behavior</h2>
 * <p>
 * {@link AbstractMcpServer} is the base for every server in this package. It
 * stores an optional project directory, reads the {@code enabledTools}
 * configuration value, and provides the contract for registering tools and
 * starting a server. A tool function receives decoded request arguments, the
 * configured project directory, and the active
 * {@code org.machanism.macha.core.commons.configurator.Configurator}. Functions
 * invoked through a stateful synchronous exchange also receive the exchange
 * session identifier. The
 * {@link org.machanism.machai.process.tools.FunctionToolsLoader} discovers and registers
 * the available function definitions, optionally restricted by
 * {@code enabledTools}.
 * </p>
 *
 * <h2>Adapters and MCP features</h2>
 * <p>
 * {@link GenericGenaiAdapter} converts
 * {@code org.machanism.machai.process.tools.ParamDescriptor} metadata into MCP
 * JSON Schema tool definitions and turns function results or failures into
 * {@code io.modelcontextprotocol.spec.McpSchema.CallToolResult} values.
 * {@link AbstractPromptGenaiAdapter} adds the
 * corresponding prompt registration and converts string, list, or serialized
 * function results into prompt messages. The concrete
 * {@link StdioGenaiAdapter}, {@link HttpStreamableGenericGenaiAdapter}, and
 * {@link HttpStatelessGenericGenaiAdapter} implementations build the
 * transport-specific specifications. The stateless adapter additionally maps
 * resource definitions to resource-read handlers; those handlers pass the
 * requested resource URI, project directory, and configurator to the function.
 * </p>
 *
 * <h2>Transport implementations</h2>
 * <ul>
 * <li>{@link StdioMcpServer} builds a single-session synchronous MCP server
 * over standard input and output. It advertises tools, prompts, and logging and
 * installs a shutdown hook that closes the built server.</li>
 * <li>{@link HttpStatelessMcpServer} builds a stateless synchronous server over
 * a Jetty servlet. It advertises tools, prompts, and resources, and is suitable
 * when requests do not require a server-side session.</li>
 * <li>{@link HttpStreamableMcpServer} builds a streamable synchronous server
 * over a Jetty servlet and advertises tools and prompts while retaining the
 * transport session context.</li>
 * </ul>
 * <p>
 * {@link AbstractHttpMcpServer} supplies the shared Jetty connector, thread-pool
 * setup, servlet registration, and port configuration used by both HTTP
 * implementations. HTTP startup failures are reported as
 * {@link McpServerStartupException}.
 * </p>
 *
 * <h2>Command-line usage</h2>
 * <p>
 * {@link McpServer} is the application entry point. Use {@code -n} or
 * {@code --name} and {@code -v} or {@code --version} to set server metadata;
 * use {@code -d} or {@code --projectDir} to provide the project directory; and
 * use {@code -c} or {@code --config} to load a properties file. When no config
 * path is supplied, the launcher attempts {@link McpServer#MCP_CONFIG_FILE_NAME}
 * ({@code mcp.properties}) and tolerates its absence. With no {@code -p} or
 * {@code --port}, it starts {@link StdioMcpServer}. Supplying a port starts
 * {@link HttpStatelessMcpServer}; adding {@code -s} or {@code --session} selects
 * {@link HttpStreamableMcpServer} instead.
 * </p>
 *
 * @author Viktor Tovstyi
 * @since 1.2.0
 */
package org.machanism.machai.mcp.server;
